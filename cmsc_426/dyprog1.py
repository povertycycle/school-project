from PIL import Image
import numpy as np
from matplotlib import pyplot as plt
import sys
import math

# Infinity
Infinity = math.inf

# Sys argv.
# Call the method with file image in the same path as this code is.
# i.e. python dyprog1.py image_file_name how_many_times_you_want_the_code_to_be_run
image_file_name = sys.argv[1]
times = sys.argv[2]

# open the image
image_input = Image.open(image_file_name).convert('L')

# Returns a path i.e. list of tuples of the coordinates of pixels need to be removed.
def pathfinder(width, height, gradient_intensity, lowest_cost):
    starting_point_from_bottom = (-10, -10)
    path = []
    x, y = (-10, -10)
    least = Infinity
    for i in range(width):
        cost, source = lowest_cost[(i, height - 1)]
        if cost < least:
            least = cost
            starting_point_from_bottom = (i, height - 1)
            x, y = source
    
    path.append(starting_point_from_bottom)
    path.append((x, y))

    while(y != 0):
        cost, source = lowest_cost[(x, y)]
        x, y = source
        path.append((x, y))
    
    return path

# Sets the gradient_intensity and lowest_cost.
def lowCost(image, gradient_intensity, lowest_cost):
    copy = image
    width, height = copy.size
    pixel = copy.load()
    # get the gradient magnitudes
    for i in range(width):
        for j in range(height):
            gm, go = magnitude_orientation(i, j, width, height, pixel)
            gradient_intensity[(i,j)] = gm

    # initialize the costs to infinity except the first rows
    for i in range(width - 1):
        lowest_cost[(i, 0)] = (gradient_intensity[(i, 0)], (i, 0))
    for j in range(1, height):
        for i in range(width):
            lowest_cost[(i, j)] = (Infinity, (-10, -10))

    # set the lowest costs
    for j in range(0, height - 1):
        for i in range(width - 1):
            thiscost = gradient_intensity[(i, j)]
            cost_middle, source_middle = lowest_cost[(i, j + 1)]
            cost_to_middle = thiscost + gradient_intensity[(i, j + 1)]
            if cost_to_middle < cost_middle:
                lowest_cost[(i, j + 1)] = (cost_to_middle, (i, j))
            # check left boundary
            if i==0:
                cost_right, source_right = lowest_cost[(i + 1, j + 1)]
                cost_to_right = thiscost + gradient_intensity[(i + 1, j + 1)]
                if cost_to_right < cost_right:
                    lowest_cost[(i + 1, j + 1)] = (cost_to_right, (i, j))
            # check right boundary
            elif i==width:
                cost_left, source_left = lowest_cost[(i - 1, j + 1)]
                cost_to_left = thiscost + gradient_intensity[(i - 1, j + 1 )]
                if cost_to_left < cost_left:
                    lowest_cost[(i - 1, j + 1)] = (cost_to_left, (i, j))
            # check middle section
            else:
                cost_left, source_left = lowest_cost[(i - 1, j + 1)]
                cost_right, source_right = lowest_cost[(i + 1, j + 1)]
                cost_to_left = thiscost + gradient_intensity[(i - 1, j + 1)]
                cost_to_right =thiscost + gradient_intensity[(i + 1, j + 1)]
                if cost_to_left < cost_left:
                    lowest_cost[(i - 1, j + 1)] = (cost_to_left, (i, j))
                if cost_to_right < cost_right:
                    lowest_cost[(i + 1, j + 1)] = (cost_to_right, (i, j))
                    
# Compute magnitude and orientation of a pixel
def magnitude_orientation(i, j, width, height, pix):
    left = 0
    right = 0
    top = 0
    bottom = 0
    if (i-1) >= 0:
        left = pix[i-1,j]
    if (i+1) <= width - 1:
        right = pix[i+1,j]
    if (j-1) >= 0:
        bottom = pix[i,j-1]
    if (j+1) <= height - 1:
        top = pix[i,j+1]
    Gx = right - left
    Gy = bottom - top

    Gm = math.sqrt((Gx**2) + (Gy**2))
    Go = (math.atan2(Gy, Gx) * 180 / np.pi) % 360
    if Go > 180:
        Go = Go - 180

    return Gm, Go

# Set line
def setline(im, s, line):
    for i, p in enumerate(s):
        im.putpixel((i, line), p)

copy = image_input.copy()
# check least cost in the last row
for v in range(int(times)):
    iter_width, iter_height = copy.size
    gradient_intensity = {}
    lowest_cost = {}
    iterator_image = Image.new('L', (iter_width-1, iter_height), 0)
    pixel = iterator_image.load()
    pixel2 = copy.load()
    lowCost(copy, gradient_intensity, lowest_cost)
    path = pathfinder(iter_width, iter_height, gradient_intensity, lowest_cost)
    d = {}
    for x, y in path:
        d[y] = x
    for line in range(iter_height):
        s = []
        for i in range(iter_width):
            if i != d[line]:
                s.append(copy.getpixel((i, line)))
        setline(iterator_image, s, line)
    copy = iterator_image.copy()


copy.save("cropped " + image_file_name) 