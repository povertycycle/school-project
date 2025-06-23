"""
File: gs.py
Author: Dana Nau <nau@cs.umd.edu>, Sept. 18, 2017

This program should work in both Python 2.7 and Python 3.6.  It includes:
 1. Domain-independent graph-search algorithm
 2. Domain-specific code for the Romanian Map Problem

Here are some things you can try:

gs.search('Arad', {'Bucharest'}, 'df', 0) # depth-first, silent
gs.search('Arad', {'Bucharest'}, 'bf')    # breadth-first, print some info
gs.search('Arad', {'Bucharest'}, 'uc', 2) # uniform-cost, print more info
gs.search('Arad', {'Bucharest'}, 'gbf',3) # GBFS, pause at each iteration
gs.search('Arad', {'Bucharest'}, 'a*', 3) # A*, pause at each iteration
gs.search('Arad', {'Nowhere'},   'uc', 2) # uniform-cost, unsolvable problem

The 1st argument is the starting state, and the 2nd one is a set of goal states.
The 3rd one is the search strategy. Its possible values are 'bf' (best first),
'df' (depth first), 'uc' (uniform cost), 'gbf' (greedy best first), and 'a*'.

The 4th argument is optional. It tells how much information to give the user:
    0 - silent, just return the answer
    1 - print info before and after the search (this is the default)
    2 - print the above, and some info at each iteration
    3 - print the above, and pause at each iteration

Notes:
(1) The h function in this file only works when the goal is Bucharest. 
    To use gbf and a* with a different goal, you'll need to redefine h.
(2) To run this program on your own problem domain, you'll need to redefine
    the neighbors and h functions.
"""

from __future__ import print_function # Make python2 use python3's print function
import sys                            # We need flush() and readline()

class Node():
    """
    Args are current state, parent node, and edge cost. Each node will include
    an ID number, state, parent, list of children, depth, g value, and h value
    """
    def __init__(self,state,parent,cost):
        global node_count
        self.state = state
        self.parent = parent
        node_count += 1                     # number of nodes created so far
        self.id = node_count                # this node's ID number
        if parent:
            parent.children.append(self)
            self.depth = parent.depth + 1   # depth in the search tree
            self.g = parent.g + cost        # total accumulated cost 
        else: 
            self.depth = 0
            self.g = cost
        self.children = []
        self.h = h(state)

def getpath(y):
    """Return the path from the root to y"""
    path = [y.state]
    while y.parent:
        y = y.parent
        path.append(y.state)
    path.reverse()
    return path

# For each search strategy there's a description of what happens, 
# a key for sorting the frontier, and a flag saying whether to use reverse order.
sort_options = {
    # Format for each line:   strategy: (description, key, reverse)
    'bf':   ('increasing node number', lambda x: x.id,      False),
    'df':   ('decreasing node number', lambda x: x.id,      True),
    'uc':   ('increasing g-value',     lambda x: x.g,       False),
    'gbf':  ('increasing h-value',     lambda x: x.h,       False),
    'a*':   ('increasing f-value',     lambda x: x.g + x.h, False)}


def expand(x, explored, frontier, key_func, reverse, verbose):
    """Add x's children to frontier, except for the pruned ones."""
    # The 'neighbors' function should return a list of (neighbor,distance) pairs
    new = [Node(newstate, x, cost) for (newstate,cost) in neighbors(x.state)]
    
    # make two lists: nodes to prune, and nodes to keep
    explored_states = [v.state for v in explored]
    prune = [m for m in new if m.state in explored_states]
    new = [m for m in new if not m.state in explored_states]

    if verbose >= 2:
        printnodes('        add {}:'.format(len(new)), new, key_func)
        printnodes('      prune {}:'.format(len(prune)), prune, key_func)
    frontier.extend(new)
    frontier.sort(key=key_func,reverse=reverse)
    return (frontier, prune)

def search(s0, goal_states, strategy, verbose=1):
    """
    Graph search for a path from s0 to any state in goal_states. 
    strategy is 'bf', 'df', 'uc', 'gbf', or 'a*'.
    verbose is a number telling how much information to print out.
    """
    global node_count               # global variables need to be declared
    node_count = 0                  # number of nodes generated
    prunes = 0                      # number of nodes pruned
    frontier = [Node(s0, None, 0)]  # "None" means there's no parent
    explored = []                   # all nodes that have been expanded
    iteration = 0
    (description, key_func, reverse) = sort_options[strategy]
    if verbose >= 1:
        print(' -- {} graph search: sort frontier by {}.'.format(strategy, \
            description))
    while frontier:
        iteration += 1              # keep track of how many iterations
        if verbose >= 2:
            printnodes('{:4}.'.format(iteration), frontier, key_func)
        x = frontier.pop(0)
        explored.append(x)
        if x.state in goal_states: 
            return success(x, node_count, prunes, explored, frontier, verbose)
        (frontier, prune) = expand(x, explored, frontier, key_func, reverse, verbose)
        prunes += len(prune)
        if verbose >= 3:
            print("> ", end='')
            sys.stdout.flush()
            sys.stdin.readline()
        elif verbose >= 2:  print('')   # kluge
    return failure(node_count, prunes, explored, frontier, verbose)


def printnodes(message, nodes, key_func):
    """For each node in nodes, print its state and its 'key_func' value"""
    nodenames = ['{0} {1}'.format(y.state,key_func(y)) for y in nodes]
    print(message, ', '.join(nodenames))


def success(x, node_count, prunes, explored, frontier, verbose):
    """called after a successful search, to print some info about it"""
    path = getpath(x)
    if verbose >= 1:
        # Path length = number of actions = (length of list) - 1
        print(' -- Path length {}, cost {}. Generated {}, pruned {}, explored {}, frontier {}.'.format(len(path)-1, x.g, node_count, prunes, len(explored), len(frontier)))
    return path


def failure(node_count, prunes, explored, frontier, verbose):
    if verbose >= 1:
        print(' -- No solution.', end=' ')
        print('Generated {}, pruned {}, explored {}, frontier {}.'.format( \
            node_count, prunes, len(explored), len(frontier)))
    return None

"""
Domain definition for the Romanian map problem. A domain definition must provide
a function 'neighbors(s)' that returns a list [(s1,c1), (s2,c2), ..., (sn,cn)], 
each (s_i,c_i) being a possible "next state" and cost of going there from s.
"""

# Dictionary of dictionaries. Each line is  city:{neighbor1:dist1, neighbor2:dist2, ...}
map = {'Arad':        {'Sibiu':140,'Timisoara':118,'Zerind':75},
    'Bucharest':      {'Fagaras':211,'Giurgiu':90,'Pitesti':101,'Urziceni':85},
    'Craiova':        {'Dobreta':120,'Pitesti':138,'Rimnicu Vilcea':146},
    'Dobreta':        {'Craiova':120,'Mehadia':75},
    'Eforie':         {'Hirsova':86},
    'Fagaras':        {'Bucharest':211,'Sibiu':99},
    'Giurgiu':        {'Bucharest':90},
    'Hirsova':        {'Eforie':86,'Urziceni':98},
    'Iasi':           {'Neamt':87,'Vaslui':92},
    'Lugoj':          {'Mehadia':70,'Timisoara':111},
    'Mehadia':        {'Dobreta':75,'Lugoj':70},
    'Neamt':          {'Iasi':87},
    'Oradea':         {'Sibiu':151,'Zerind':71},
    'Pitesti':        {'Bucharest':101,'Craiova':138,'Rimnicu Vilcea':97},
    'Rimnicu Vilcea': {'Craiova':146,'Pitesti':97,'Sibiu':80},
    'Sibiu':          {'Arad':140,'Fagaras':99,'Oradea':151,'Rimnicu Vilcea':80},
    'Timisoara':      {'Arad':118,'Lugoj':111},
    'Urziceni':       {'Bucharest':85,'Hirsova':98,'Vaslui':142},
    'Vaslui':         {'Iasi':92,'Urziceni':142},
    'Zerind':         {'Arad':75,'Oradea':71},
    'Nowhere':        {}}        # so I can demonstrate unsolvable problems

def neighbors(city):
    """Return a list of neighboring cities and their distances from city"""
    return map[city].items()   # change the dictionary into a list of pairs

### Heuristic function: straight-line distance to Bucharest
sld = {
     'Arad':        366,
     'Bucharest':   0,
     'Craiova':     160,
     'Dobreta':     242,
     'Eforie':      161,
     'Fagaras':     176,
     'Giurgiu':     77,
     'Hirsova':     151,
     'Iasi':        226,
     'Lugoj':       244,
     'Mehadia':     241,
     'Neamt':       234,
     'Oradea':      380,
     'Pitesti':     100,
     'Rimnicu Vilcea':  193,
     'Sibiu':       253,
     'Timisoara':   329,
     'Urziceni':    80,
     'Vaslui':      199,
     'Zerind':      374,
     'Nowhere':     float('inf')}        # so I can demonstrate unsolvable problems

def h(city):
    """
    'h' function for the Romanian map problem. 
    It returns the straight-line distance from city to Bucharest.
    """
    return sld[city]
