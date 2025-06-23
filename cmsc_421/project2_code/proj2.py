"""
File: proj2.py -- Dana, April 24, 2018
A simple-minded proj2 program. It uses racetrack.py to find a path to the goal
ignoring the possibility of steering erros, and returns the first move in this path.
"""
from __future__ import print_function  
import racetrack,math
import heuristics
import sys
import json
import time
import random
import numpy
import sample_probs

h_layers = 2
Q = {}
envelop = set()
ns = {}
nsa = {}
freq = 10
inner_loop = 20
infinity = math.inf
grid = []
crash_penalty = 100

# global V←V0
def v0(state,fline,walls):
    v0 = h_walldist(state,fline,walls)
    return v0

def h_walldist(state, fline, walls):
    """
    The first time this function is called, for each gridpoint that's not inside a wall
    it will cache a rough estimate of the length of the shortest path to the finish line.
    The computation is done by a breadth-first search going backwards from the finish 
    line, one gridpoint at a time.
    
    On all subsequent calls, this function will retrieve the cached value and add an
    estimate of how long it will take to stop. 
    """

    ((x,y),(u,v)) = state
    hval = float(grid[x][y])
    
    # add a small penalty to favor short stopping distances
    au = abs(u); av = abs(v); 
    sdu = au*(au-1)/2.0
    sdv = av*(av-1)/2.0
    sd = max(sdu,sdv)
    penalty = sd/10.0

    # compute location after fastest stop, and add a penalty if it goes through a wall
    if u < 0: sdu = -sdu
    if v < 0: sdv = -sdv
    sx = x + sdu
    sy = y + sdv
    if racetrack.crash([(x,y),(sx,sy)],walls):
        penalty += math.sqrt(au**2 + av**2)
    hval = max(hval+penalty,sd)
    return hval

def edist_grid(fline,walls):
    global grid, g_fline, g_walls, xmax, ymax
    xmax = max([max(x,x1) for ((x,y),(x1,y1)) in walls])
    ymax = max([max(y,y1) for ((x,y),(x1,y1)) in walls])
    grid = [[edistw_to_line((x,y), fline, walls) for y in range(ymax+1)] for x in range(xmax+1)]
    flag = True
    print('computing edist grid', end=' '); sys.stdout.flush()
    while flag:
        print('.', end=''); sys.stdout.flush()
        flag = False
        for x in range(xmax+1):
            for y in range(ymax+1):
                for y1 in range(max(0,y-1),min(ymax+1,y+2)):
                    for x1 in range(max(0,x-1),min(xmax+1,x+2)):
                        if grid[x1][y1] != infinity and not racetrack.crash(((x,y),(x1,y1)),walls):
                            if x == x1 or y == y1:
                                d = grid[x1][y1] + 1
                            else:
                                d = grid[x1][y1] + 1.4142135623730951
                            if d < grid[x][y]:
                                grid[x][y] = d
                                flag = True
    print(' done')
    g_metric = 'edist'
    g_fline = fline
    g_walls = walls
    return grid


def edistw_to_line(point, edge, walls):
    """
    straight-line distance from (x,y) to the line ((x1,y1),(x2,y2)).
    Return infinity if there's no way to do it without intersecting a wall
    """
#   if min(x1,x2) <= x <= max(x1,x2) and  min(y1,y2) <= y <= max(y1,y2):
#       return 0
    (x,y) = point
    ((x1,y1),(x2,y2)) = edge
    if x1 == x2:
        ds = [math.sqrt((x1-x)**2 + (y3-y)**2) \
            for y3 in range(min(y1,y2),max(y1,y2)+1) \
            if not racetrack.crash(((x,y),(x1,y3)), walls)]
    else:
        ds = [math.sqrt((x3-x)**2 + (y1-y)**2) \
            for x3 in range(min(x1,x2),max(x1,x2)+1) \
            if not racetrack.crash(((x,y),(x3,y1)), walls)]
    ds.append(infinity)
    return min(ds)

def initialize(state,fline,walls,metric='edist'):
    print('Unfortunately, this work will be lost when the process exits.')
    grid = edist_grid(fline, walls)
    with open("cache.txt", 'w') as outfile:
        print('dumping')
        json.dump(grid, outfile)

def applicable(s,walls):
    ((x,y), (u,v)) = s
    actions = []
    for i in [-1,0,1]:
        for j in [-1,0,1]:
            actions.append((u+i,v+j))
    return actions

# γ(s,a)
# def y_states(state,action):
#     y_ret = []
#     ((x,y), (u,v)) = state
#     (new_u,new_v) = action
#     i_range = [-1,0,1]
#     j_range = [-1,0,1]
#     if (abs(new_u) <= 1):
#         i_range = [0]
#     if (abs(new_v) <= 1):
#         j_range = [0]
#     for i in i_range:
#         for j in j_range:
#             y_ret.append(((x+new_u+i,y+new_v+j),(new_u,new_v)))
#     return y_ret

# def pr(s_,s,a):
# 	((real_x,real_y),(_,_)) = s_
# 	((x,y),(_,_)) = s
# 	real_u = real_x - x
# 	real_v = real_y - y
# 	(u,v) = a
# 	pr_u = 0
# 	pr_v = 0
# 	if (abs(u) <= 1):
# 		pr_u = 1
# 	else:
# 		if real_u == u:
# 			pr_u = 0.6
# 		else:
# 			pr_u = 0.2
# 	if (abs(v) <= 1):
# 		pr_v = 1
# 	else:
# 		if real_v == v:
# 			pr_v = 0.6
# 		else:
# 			pr_v = 0.2
# 	return pr_u*pr_v

# sample
# def Sample(s, a, fline, walls):
# 	states = y_states(s, a)
# 	states_index = range(len(states))
# 	draws = numpy.random.choice(states_index, 1, p=[pr(s_,s,a) for s_ in states])
# 	return states[draws[0]]

def UCT(s, fline, walls, h ):
    ret = None
    for i in range(inner_loop):
        ((x,y),(u,v)) = s
        # if s is Sg then return 0
        if racetrack.goal_test(s, fline):
            return 0
        # if h = 0 then return V0(s)
        if h == 0:
            return v0( s, fline, walls)

        applicables = applicable(s, walls)
        
        # if s not in envelope then do
        if (s not in envelop):
            # add s to envelope
            envelop.add(s)
            # n(s) 0
            ns[s] = 0
            # for all a in Applicable(s) do
            for a in applicables:
                # Q(s,a) = 0
                Q[(s, a)] = 0
                # n(s,a) 0
                nsa[(s, a)] = 0

        # Untried {a 2 Applicable(s) | n(s, a) = 0}
        untried = []
        for a in applicables:
            if (nsa[(s, a)] == 0):
                untried.append(a)
        # if Untried 6= ? then a ̃ Choose(Untried)\
        a_bar = None

        # if (s == ((1,1),(-1,0))):
        #    print(untried)

        if (untried):
            # a ̃ Choose(Untried)
            a_bar = random.choice(untried)
        else:
            #  ̃ argmina2Applicable(s){Q(s,a) C ⇥[log(n(s))/n(s,a)]2 }
            act_min = None
            q_min  = math.inf

            for a in applicables:
                # {Q(s,a) C ⇥[log(n(s))/n(s,a)]2 }
                # q  = Q[(s, a)] - 1/h * (math.log(ns[s])/nsa[(s, a)])**0.5
                q = Q[(s, a)] - 2 * ((math.log(ns[s])/nsa[(s, a)]) ** 0.5)
                if(q_min > q):
                    q_min = q
                    act_min = a

            a_bar = act_min
            # print(a_bar)

        # s Sample(⌃, s, a ̃)
        # s_ = Sample(s, a_bar, fline, walls)
        e = steering_error(a_bar)
        s_ = ((x+a_bar[0]+e[0], y+a_bar[1]+e[1]), (a_bar[0], a_bar[1]))
        if racetrack.crash(((x,y), (x+a_bar[0]+e[0], y+a_bar[1]+e[1])), walls):
            cost_rollout = crash_penalty
        else:
            # cost-rollout cost(s, a ̃) + UCT(s, h - 1)
            cost_rollout = 1 + UCT(s_, fline, walls, h - 1)
        # Q(a,a) = n(s,a) * Q(s, a ̃) + cost-rollout]/(1 + n(s, a ̃))
        Q[(s, a_bar)] = (nsa[(s, a_bar)] * Q[(s, a_bar)]  + cost_rollout)/(1 + nsa[(s, a_bar)])
        #if (s == ((1,1),(-1,0))):
        #    print(s, a_bar, Q[(s, a_bar)])
        # ns += 1
        ns[s] = ns[s] + 1
        # n(s, a ̃) + 1
        nsa[(s, a_bar)] = nsa[(s, a_bar)] + 1
        #return cost-rollout
        ret = cost_rollout
    return ret

def steering_error(z):
    (u,v) = z
    if abs(u) > 1:
        ue = random.choice([-1, 0, 0, 0, 1])
    else:
        ue = 0
    if abs(v) > 1:
        ve = random.choice([-1, 0, 0, 0, 1])
    else:
        ve = 0
    return (ue, ve)

def main(s, fline, walls):
    global grid
    with open('cache.txt') as json_file:
        grid = json.load(json_file)
    i = 1
    act_min_old = None
    while(True):
        # UCT
        UCT(s, fline, walls, h_layers)
        if (i%freq == 0):
            act_min = None
            q_min = math.inf
            for act in applicable(s, walls):
                if (s, act) in Q:
                    q = Q[(s, act)]
                    if q < q_min:
                        act_min = act
                        q_min = q
            if(act_min == None):	
                continue
            if act_min == act_min_old:
                return act_min
            else:
                act_min_old = act_min
            f = open('choices.txt', 'w')
            print(act_min ,file=f,flush=True)
            #print('choice: '+str(act_min))
            #print(Q)
        i += 1

# [(2,1), [(4,1),(5,1)], [[(0,0),(8,0)], [(8,0),(8,8)], [(8,8),(0,8)], [(0,8),(0,0)], [(4,0),(4,5)]]]
if __name__ == "__main__":
    with open('cache.txt') as json_file:
        grid = json.load(json_file)
    i = 1
    act_min_old = None
    for i in range(1000):
        UCT(((2,1),(0,0)), [(4,1),(5,1)], [[(0,0),(8,0)], [(8,0),(8,8)], [(8,8),(0,8)], [(0,8),(0,0)], [(4,0),(4,5)]], 2)
    #print(Q)














