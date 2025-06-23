import sys
from PIL import Image

image_1 = sys.argv[1]
image_2 = sys.argv[2]

# open the image
image1 = Image.open(image_1).convert('L')
image2 = Image.open(image_2).convert('L')

def match(l1, l2):
    occ = 250
    c = {}
    o = {}
    n = len(l1) + 1
    m = len(l2) + 1
    for i in range(n):
        for j in range(m):
            c[(i, j)] = 0
    for i in range(n):
        for j in range(m):
            if i==0 and j==0:
                o[(i, j)] = (-1, -1)
                c[(i, j)] = 0
            elif i==0:
                o[(i, j)] = (i, j - 1)
                c[(i, j)] = occ * j
            elif j==0:
                o[(i, j)] = (i - 1, j)
                c[(i, j)] = occ * i
            else:
                cost_to_occleft = c[(i - 1, j)] + occ
                cost_to_occright = c[(i, j - 1)] + occ
                cost_to_match = c[(i - 1, j - 1)] + (l1[i - 1] - l2[j - 1])**2
                minimum = min(cost_to_occleft, cost_to_occright, cost_to_match)
                if (minimum == cost_to_match):
                    o[(i, j)] = (i - 1, j - 1)
                elif (minimum == cost_to_occleft):
                    o[(i, j)] = (i - 1, j)
                elif (minimum == cost_to_occright):
                    o[(i, j)] = (i, j - 1)
                c[(i, j)] = minimum
    return o

# Set line
def setline(im, s, line):
    for i, p in enumerate(s):
        if p == -1:
            im.putpixel((i, line), 0)
        else:
            im.putpixel((i, line), p * 4)

# Make line
def makeLine(pix, width, height):
    line = []
    for i in range (width):
        line.append(pix[i, height])
    return line

def pathfinder(origins, starting_point):
    x, y = starting_point
    path = []
    while x != 0 and y != 0:
        (xP, yP) = origins[(x, y)]
        if x - xP == 1 and y - yP == 0:
            path.append(-1)
        elif x - xP == 1 and y - yP == 1:
            path.append(abs(x - y))
        x = xP
        y = yP
    path.reverse()
    return path


width1, height1 = image1.size
width2, height2 = image2.size
pix1 = image1.load()
pix2 = image2.load()
copy = Image.new('L', (width1, height1), 0)
pixel = copy.load()

for i in range(height1):
    line1 = makeLine(pix1, width1, i)
    line2 = makeLine(pix2, width2, i)
    origins = match(line1, line2)
    start_point = (len(line1) - 1, len(line2) - 1)
    path = pathfinder(origins, start_point)
    setline(copy, path, i)

    
    
copy.save("stereo " + image_1 + " and " + image_2) 