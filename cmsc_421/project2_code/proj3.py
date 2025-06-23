"""
William Sentosatio
UID 114545749
I pledge on my honor that I have not given or received any unauthorized assistance on this assignment/examination.
"""
import math
import random
import sys
import os.path
import pickle
import os
################################################################################
# Important note: Always delete grid.pkl for every problem because the file    #
# only works for the specific problem, so every time a new problem is entered  #
# into the initialization, a new grid with 'hval's needs to be created.        #
# Apparently, only iterative deepening with d = 0 to 1 works for every problem #
# The rest of the problem will either crash or not finish with d = 2 or d = 3. #
################################################################################
################################################################################
# Global variables.                                                            #
################################################################################
finish_line = []                            # Global finish line.
global_walls = []                           # Global walls.
seen = []                                   # Seen for UCT.
tried = {}                                  # Tried for UCT.
avg_cost = {}                               # Average Cost / Q for UCT.
infinity = float('inf')                     # Infinity.
################################################################################
# Global variables for edist_grid.                                             #
################################################################################
g_fline = False                             # Global if finish line.
g_walls = False                             # Global if walls.
grid = []                                   # Global grid.
xmax = 0                                    # Global maximum x of the walls.
ymax = 0                                    # Global maximum y of the walls.
################################################################################
# initialize.                                                                  #
################################################################################
def initialize(state,fline,walls):
    global grid, g_fline, g_walls, xmax, ymax
    print('Unfortunately, this work will be lost when the process exits.')
    print('Write grid into a file and call if it exists.')
    # Checks if the file exists.
    # try:
    #     f = pickle.load(open("grid.pkl", "rb"))
    #     # This is where the global variables are stored.
    #     grid = f['grid']
    #     g_fline = f['g_fline']
    #     g_walls = f['g_walls']
    #     xmax = f['xmax']
    #     ymax = f['ymax']
    # # Create the file if it doesn't.
    # except FileNotFoundError:
    edist_grid(fline, walls)
    output = open("grid.pkl", "wb")
    # This is where the global variables come from.
    pickle.dump({'grid': grid, 'g_fline':g_fline, 'g_walls': g_walls, 'xmax': xmax, 'ymax': ymax}, output)
    output.close()
################################################################################
# Euclidean distance from point to edge.                                       #
################################################################################
def edist_to_line(point, edge):
    (x,y) = point
    ((x1,y1),(x2,y2)) = edge
    if x1 == x2:
        ds = [math.sqrt((x1-x)**2 + (y3-y)**2) for y3 in range(min(y1,y2),max(y1,y2)+1)]
    else:
        ds = [math.sqrt((x3-x)**2 + (y1-y)**2) for x3 in range(min(x1,x2),max(x1,x2)+1)]
    return min(ds)
################################################################################
# Returns the list of possible states from a state.                            #
################################################################################
def next_states(state,walls):
    states = []
    (loc,(vx,vy)) = state
    for dx in [0,-1,1]:
        for dy in [0,-1,1]:
            (wx,wy) = (vx+dx,vy+dy)
            newloc = (loc[0]+wx,loc[1]+wy)
            if not crash((loc,newloc),walls):
                states.append((newloc,(wx,wy)))
    return states
################################################################################
# Checks if a state is in the goal line.                                       #
################################################################################
def goal_test(state,f_line):
    return state[1] == (0,0) and intersect((state[0],state[0]), f_line)
################################################################################
# Checks if a move will cause a crash.                                         #
################################################################################
def crash(move,walls):
    for walls in walls:
        if intersect(move,walls): return True
    return False
################################################################################
# Check if two edges intersect.                                                #
################################################################################
def intersect(e1,e2):
    ((x1a,y1a), (x1b,y1b)) = e1
    ((x2a,y2a), (x2b,y2b)) = e2
    dx1 = x1a-x1b
    dy1 = y1a-y1b
    dx2 = x2a-x2b
    dy2 = y2a-y2b
    if (dx1 == 0) and (dx2 == 0):
        if x1a != x2a: return False
        else:
            return collinear_point_in_edge((x1a,y1a),e2) or collinear_point_in_edge((x1b,y1b),e2) or collinear_point_in_edge((x2a,y2a),e1) or collinear_point_in_edge((x2b,y2b),e1)
    if (dx2 == 0):
        x = x2a
        y = (x2a-x1a)*dy1/float(dx1) + y1a
        return collinear_point_in_edge((x,y),e1) and collinear_point_in_edge((x,y),e2)
    elif (dx1 == 0):
        x = x1a
        y = (x1a-x2a)*dy2/float(dx2) + y2a
        return collinear_point_in_edge((x,y),e1) and collinear_point_in_edge((x,y),e2)
    else:
        if dy1*dx2 == dx1*dy2:
            if dx2*dx1*(y2a-y1a) != dy2*dx1*x2a - dy1*dx2*x1a:
                return False
            return collinear_point_in_edge((x1a,y1a),e2) or collinear_point_in_edge((x1b,y1b),e2) or collinear_point_in_edge((x2a,y2a),e1) or collinear_point_in_edge((x2b,y2b),e1)
        x = (dx2*dx1*(y2a-y1a) - dy2*dx1*x2a + dy1*dx2*x1a)/float(dx2*dy1 - dy2*dx1)
        y = (dy2*dy1*(x2a-x1a) - dx2*dy1*y2a + dx1*dy2*y1a)/float(dy2*dx1 - dx2*dy1)
    return collinear_point_in_edge((x,y),e1) and collinear_point_in_edge((x,y),e2)
################################################################################
# Helps intersect method.                                                      #
################################################################################
def collinear_point_in_edge(point, edge):
    (x,y) = point
    ((xa,ya),(xb,yb)) = edge
    if ((xa <= x <= xb) or (xb <= x <= xa)) and ((ya <= y <= yb) or (yb <= y <= ya)):
        return True
    return False
################################################################################
# Make a steering_error and returns new (u,v).                                 #
################################################################################
def steering_error(u,v):
    if abs(u) <= 1:
        q = 0
    else:
        q = random.choice([-1,0,0,0,1])
    if abs(v) <= 1:
        r = 0
    else:
        r = random.choice([-1,0,0,0,1])
    return (q,r)
################################################################################
# Returns heuristics for a state.                                              #
################################################################################
def h_walldist(state, fline, walls):
    global g_fline, g_walls, grid, g_fline, g_walls, xmax, ymax

    ((x,y),(u,v)) = state
    hval = float(grid[x][y])

    au = abs(u); av = abs(v)
    sdu = au*(au-1)/2.0
    sdv = av*(av-1)/2.0
    sd = max(sdu,sdv)
    penalty = sd

    if u < 0: sdu = -sdu
    if v < 0: sdv = -sdv
    sx = x + sdu
    sy = y + sdv
    if crash([(x,y),(sx,sy)],walls):
        penalty += math.sqrt(au**2 + av**2)
    hval = max(hval+penalty,sd)
    return hval
################################################################################
# Initialize the grid value.                                                   #
################################################################################
def edist_grid(fline,walls):
    global grid, g_fline, g_walls, xmax, ymax
    xmax = max([max(x,x1) for ((x,y),(x1,y1)) in walls])
    ymax = max([max(y,y1) for ((x,y),(x1,y1)) in walls])

    grid = [[edistw_to_finish((x,y), fline, walls) for y in range(ymax+1)] for x in range(xmax+1)]
    flag = True
    while flag:
        flag = False
        for x in range(xmax+1):
            for y in range(ymax+1):
                for y1 in range(max(0,y-1),min(ymax+1,y+2)):
                    for x1 in range(max(0,x-1),min(xmax+1,x+2)):
                        if grid[x1][y1] != infinity and not crash(((x,y),(x1,y1)),walls):
                            if x == x1 or y == y1:
                                d = grid[x1][y1] + 1
                            else:
                                d = grid[x1][y1] + 1.4142135623730951
                            if d < grid[x][y]:
                                grid[x][y] = d
                                flag = True
    g_fline = fline
    g_walls = walls
    return grid
################################################################################
# Distance from a finish line to a point.                                      #
################################################################################
def edistw_to_finish(point, fline, walls):
    (x,y) = point
    ((x1,y1),(x2,y2)) = fline
    if x1 == x2:
        ds = [math.sqrt((x1-x)**2 + (y3-y)**2) for y3 in range(min(y1,y2),max(y1,y2)+1) if not crash(((x,y),(x1,y3)), walls)]
    else:
        ds = [math.sqrt((x3-x)**2 + (y1-y)**2) for x3 in range(min(x1,x2),max(x1,x2)+1) if not crash(((x,y),(x3,y1)), walls)]
    ds.append(infinity)
    return min(ds)
################################################################################
# ExpectiMin                                                                    #
################################################################################
def expectmin(x, d, transposition):
    global finish_line, global_walls
    state, turn = x
    pos, speed = state
    # return 0 as it reaches the finish line
    if (goal_test(state, finish_line)):
        
        return (-1000, (0, 0))
    # If depth is 0, return the heuristic value for that node.
    elif (d == 0):
        cost = h_walldist(state, finish_line, global_walls)
        sx, sy = pos
        u, v = speed
        newloc = (sx + u, sy + v)
        if crash((pos, newloc), global_walls):
            cost = cost + 10
        return (cost, speed)
    # If it is our turn...
    elif(turn == True):
        next = next_states(state, global_walls)
        children = []
        costs = {}
        best_speed = (-10, -10)
        minimum = 100000
        # Make the new nodes from the possible next states.
        for item in next:
            children.append((item, False))
        # Do every expectmin for every children.
        for item in children:
            state_exist, current_turn = item
            # Check the transposition table for existing state.
            try:
                current_cost, current_speed = transposition[state_exist]
            # Put the value if the key does not exist.
            except KeyError:
                current_cost, current_speed = expectmin(item, d - 1, transposition)
                transposition[state_exist] = (current_cost, current_speed)

            sx, sy = pos
            u, v = current_speed
            newloc = (sx + u, sy + v)
            if crash((pos, newloc), global_walls):
                current_cost = current_cost + 10
            costs[current_cost] = current_speed
            
        # Get the minimum cost
        minimum = min(costs)
        # Get the minimum speed
        best_speed = costs[min(costs)]
        return (minimum, best_speed)
    # The error's turn
    else:
        costs = 0
        children = []
        ((x1, y1), (u, v)), turn = x
        for eu in [-1, 0, 1]:
            for ev in [-1, 0, 1]:
                newstate = ((x1 + u + eu, y1 + v + ev) , (u, v))
                new_x = (newstate, True)
                costs = costs + (expectmin(new_x, d - 1, transposition)[0] * pr(eu, ev))
        return (costs, (u, v))
################################################################################
# Small function to help Pr[y|x].                                              #
################################################################################
def pr(eu, ev):
    # Probability function
    pr_u = 0
    pr_v = 0
    # 0.2 for -1 and 1
    if (eu == -1 or eu == 1):
        pr_u = 0.2
    # 0.6 for 0
    elif (eu == 0):
        pr_u = 0.6
    if (ev == -1 or ev == 1):
        pr_v = 0.2
    elif (ev == 0):
        pr_v = 0.6
    return pr_u * pr_v
################################################################################
# Main function.                                                               #
################################################################################
def main(state,finish,walls):
    global finish_line, global_walls, grid, g_fline, g_walls, xmax, ymax

    f = pickle.load(open("grid.pkl", "rb"))
    # This is where the global variables are stored.
    grid = f['grid']
    g_fline = f['g_fline']
    g_walls = f['g_walls']
    xmax = f['xmax']
    ymax = f['ymax']
        
    # Updates global finish lines and global walls.
    for item in finish:
        finish_line.append(item)
    for item in walls:
        global_walls.append(item)
    # Node x is a tuple of a state and a boolean of either true or false 
    # i.e. your turn or the error turn
    your_turn = True
    x = (state, your_turn)
    # List of costs returned from the expectimin function
    costs = {}
    # Iterative deepening from d = 1 to 2
    for i in range(1, 3):
        # Transposition table that is being reset for every iterative deepening
        transposition = {}
        expectmin(x, i, transposition)
        for item in transposition:
            current_cost, current_speed = transposition[item]
            try:
                costs[current_cost]
            except KeyError:
                costs[current_cost] = current_speed
    f = open("choices.txt", "w")
    # Remove 0, 0 speed that causes the racecar to stop halfway.
    newcosts = {}
    for item in costs:
        if costs[item] == (0, 0):
            if item == -1000:
                newcosts[item] = costs[item]
        else:
            newcosts[item] = costs[item]

    # Best speed based on best cost
    min_speed = newcosts[min(newcosts)]
    print(min_speed, file = f, flush = True)
            

    



    
