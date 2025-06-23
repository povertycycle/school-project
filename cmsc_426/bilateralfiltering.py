from PIL import Image
import cv2
import numpy as np
import sys
import math

image_file_name = sys.argv[1]
sigma_square_d = sys.argv[2]
sigma_square_r = sys.argv[3]
image_file_output_name = sys.argv[4]

image_input = Image.open(image_file_name).convert("RGB")

# bilateralFilter starts here

def bilateralFilter(image, sigma_d, sigma_r):
    width, height = image.size
    copy = image
    pix = copy.load()
    for i in range(2, width - 3):
        for j in range(2, height - 3):
            a = (0, 0, 0)
            b = 0
            for k in range(i - 2, i + 3):
                for l in range(j - 2, j + 3):
                    w = domain_kernel(i, j, k, l, sigma_d) * range_kernel(i, j, k, l, sigma_r, pix)
                    red1, green1, blue1 = pix[k, l]
                    red2, green2, blue2 = a
                    a = (red1 * w + red2, green1 * w + green2, blue1 * w + blue2)
                    b = w + b
            x, y, z = a
            pix[i,j] = (int(x / b), int(y / b), int(z / b))
    return copy

def domain_kernel(a, b, c, d, sigma_square):
    d = math.exp(-( ((a - c) ** 2) + ((b - d) ** 2) ) / (2 * sigma_square))
    return d

def range_kernel(a, b, c, d, sigma_square, pixel):
    r1, g1, b1 = pixel[a,b]
    r2, g2, b2 = pixel[c,d]
    norm = math.sqrt((r1 - r2) ** 2 + (g1 - g2) ** 2 + (b1 - b2) ** 2)
    r = math.exp(-(norm ** 2) / (2 * sigma_square))
    return r


#bilateralFilter ends here

output_image = bilateralFilter(image_input, int(sigma_square_d), int(sigma_square_r))
output_image.save(image_file_output_name)
