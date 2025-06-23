#I pledge on my honor that I have not given or received
# any unauthorized assistance on this project.
# William Sentosatio UID 114545749
# Project 1 CMSC421

import math, racetrack
import sys

# borrowing heuristics'
infinity = float('inf')     # alternatively, we could import math.inf

# global variables
g_fline = False
g_walls = False
grid = []
grids = []
around_fline = []
xmax = 0
ymax = 0

def h_proj1(state, fline, walls):
	# edited h_walldist
	global g_fline, g_walls, around_fline
	if fline != g_fline or walls != g_walls or grid == []:
		edist_edited(fline, walls)
	((x,y),(u,v)) = state
	hval = float(grid[x][y])

	# add a small penalty to favor short stopping distances
	au = abs(u); av = abs(v);
	sdu = au*(au+1)/2.0
	sdv = av*(av+1)/2.0
	sd = math.sqrt(sdu**2 + sdv**2)
	if near_fline(x,y,sdu,sdv,fline) == False:
		penalty = sd/10.0
	else:
		penalty = 0

	# compute location after fastest stop, and add a penalty if it goes through a wall
	if u < 0: sdu = -sdu
	if v < 0: sdv = -sdv
	sx = x + sdu
	sy = y + sdv

	if racetrack.crash([(x,y),(sx,sy)],walls) or going_away_from_fline((x,y), (sx,sy), fline):
		penalty += math.sqrt(au**2 + av**2)

	if (sx,sy) in grids:
		return hval
	elif near_startpoint(sd):
		return hval
	else:
		hval = max(hval+penalty,sd)
		return hval

def near_startpoint(sd):
	if sd < 3:
		return True
	else:
		return False

def going_away_from_fline(c_point,f_point,fline):
	x1,y1 = c_point
	x2,y2 = f_point
	((xf1,yf1),(xf2,yf2)) = fline
	a = 0
	b = 0
	# for c_point
	if x1 == x2:
		a = [math.sqrt((xf1-x1)**2 + (y3-y1)**2) for y3 in range(min(yf1,yf2),max(yf1,yf2)+1)]
	else:
		a = [math.sqrt((x3-x1)**2 + (yf1-y1)**2) for x3 in range(min(xf1,xf2),max(xf1,xf2)+1)]
	# for f_point
	if x1 == x2:
		b = [math.sqrt((xf1-x2)**2 + (y3-y2)**2) for y3 in range(min(yf1,yf2),max(yf1,yf2)+1)]
	else:
		b = [math.sqrt((x3-x2)**2 + (yf1-y2)**2) for x3 in range(min(xf1,xf2),max(xf1,xf2)+1)]
	if a <= b:
		return True
	else:
		return False

def near_fline(x, y, u, v, fline):
	global xmax, ymax
	((x1,y1), (x2,y2)) = fline
	if max(0, x1 - 5) <= x + u <= min(x2 + 5, xmax) or max(0, y1 - 5) <= y + v <= min(y2 + 5, xmax):
		return True
	else:
		return False

def edist_edited(fline, walls):
	global grid, grids, g_fline, g_walls, xmax, ymax, around_fline
	maximum_walls(walls)
	# get the list of gridpoints around the wall corners.
	area_fline(fline, walls)
	corner_grids(fline, walls)
	# calculate the heuristics value for each gridpoint based on the edistw_to_finish
	# grids has the list of "important" coordinates
	grid = [[edistw_to_finish((x,y), fline, walls) for y in range(ymax+1)] for x in range(xmax+1)]
	# update the heuristic values for the "important" grids that does not intersect wall
	# by increasing the other gridpoints heuristic values
	if len(walls) > 4:
		for x in range(xmax+1):
			for y in range(ymax+1):
				if (x,y) not in grids:
					for (xg,yg) in grids:
						copy = grid[x][y]
						if edistw_to_finish((x,y), fline, walls) > edistw_to_finish((xg,yg), fline, walls):
							grid[x][y] = grid[x][y] + edistw_to_finish((x,y), fline, walls)
							break
						elif going_away_from_fline((xg,yg), (x,y), fline):
							grid[x][y] = grid[x][y] + edistw_to_finish((x,y), fline, walls)
							break

	#borrowing heuristics
	flag = True
	print('computing edist grid', end=' ');sys.stdout.flush()
	while flag:
		print('.', end='');sys.stdout.flush()
		flag = False
		for x in range(xmax+1):
			for y in range(ymax+1):
				for y1 in range(max(0,y-1),min(ymax+1,y+2)):
					for x1 in range(max(0,x-1),min(xmax+1,x+2)):
						if grid[x1][y1] != infinity and not racetrack.crash(((x,y),(x1,y1)),walls):
							if (x == x1 or y == y1):
								d = grid[x1][y1] + 1
							else:
								d = grid[x1][y1] + 1.4142135623730951
							if d < grid[x][y] and ((x,y) in grids):
								selection = distance_to_corner((x,y))
								for calc in selection:
									if calc < d and calc < grid[x][y]:
										grid[x][y] = calc
									else:
										grid[x][y] = d
									flag = True

	print(' done')
	g_fline = fline
	g_walls = walls
	return grid

def distance_to_corner(point):
	global grids
	dist = []
	for (x,y) in grids:
		a,b = point
		z = math.sqrt((y-b)**2 + (x-a)**2)
		dist.append(z)
	return dist


def maximum_walls(walls):
	global xmax, ymax
	xmax = max([max(x,x1) for ((x,y),(x1,y1)) in walls])
	ymax = max([max(y,y1) for ((x,y),(x1,y1)) in walls])

def area_fline(fline, walls):
	global around_fline, xmax, ymax
	((x1,y1), (x2,y2)) = fline
	for x in range(max(0, x1-5), min(xmax+1, x2+5)):
		for y in range(max(0, y1-5), min(ymax+1, y2+5)):
			around_fline.append((x,y))

def corner_grids(fline, walls):
	global grids, gfline, xmax, ymax
	((x1,y1), (x2,y2)) = fline
	# boundary of the racetrack
	# inserting into grids
	for ((x,y),(x1,y1)) in walls:
		for xs in range(max(x-1,0),min(x+2, xmax+1)):
			for ys in range(max(y-1,0),min(y+2, ymax+1)):
				if not in_walls((xs,ys), walls) and in_boundary((xs,ys), xmax, ymax):
					grids.append((xs,ys))

def in_walls(point, walls):
	for ((x,y),(x1,y1)) in walls:
		if racetrack.collinear_point_in_edge(point, ((x,y),(x1,y1))) == True:
			return True

def in_boundary(point, x, y):
	(xa, ya) = point
	if 0 < xa < x and 0 < ya < y:
		return True
	else:
		return False

# exactly touching the finish line with 0,0 velocity
def touch_fline(sd, point, fline, walls):
	if sd == edistw_to_finish(point, fline, walls):
		return True
	else:
		return False

#borrowing edistw_to_finish
def edistw_to_finish(point, fline, walls):
    """
    straight-line distance from (x,y) to the finish line ((x1,y1),(x2,y2)).
    Return infinity if there's no way to do it without intersecting a wall
    """
#   if min(x1,x2) <= x <= max(x1,x2) and  min(y1,y2) <= y <= max(y1,y2):
#       return 0
    (x,y) = point
    ((x1,y1),(x2,y2)) = fline
    # make a list of distances to each reachable point in fline
    if x1 == x2:           # fline is vertical, so iterate over y
        ds = [math.sqrt((x1-x)**2 + (y3-y)**2) \
            for y3 in range(min(y1,y2),max(y1,y2)+1) \
            if not racetrack.crash(((x,y),(x1,y3)), walls)]
    else:                  # fline is horizontal, so iterate over x
        ds = [math.sqrt((x3-x)**2 + (y1-y)**2) \
            for x3 in range(min(x1,x2),max(x1,x2)+1) \
            if not racetrack.crash(((x,y),(x3,y1)), walls)]
    ds.append(infinity)    # for the case where ds is empty
    return min(ds)
