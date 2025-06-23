import sys
import os
import torch
import numpy as np
from PIL import Image
import torch.nn as nn
from torch.autograd import Variable

# python ./script4.py image_file_name path_name
# python ./script4.py a.png C:\Users\Willi\Desktop\CMSC426.HW\hw3-dataset\

image_file_name = sys.argv[1]
path = sys.argv[2]

class Net(nn.Module):
    def __init__(self):
        super(Net, self).__init__()
        self.fc1 = nn.Conv2d(3, 15, kernel_size=5, stride=1)
        self.fc2 = nn.MaxPool2d(2, stride=None)

        self.fc3 = nn.Conv2d(15, 30, kernel_size=3, stride=1)

        self.fc4 = nn.Conv2d(30, 40, kernel_size=3, stride=1)

        self.fc5 = nn.Linear(3360, 20)
        self.fc6 = nn.Linear(20, 2)
        self.relu = nn.ReLU(inplace=True)
    def forward(self, x):
        x = self.fc1(x)
        x = self.relu(x)
        x = self.fc2(x)

        x = self.fc3(x)
        x = self.relu(x)
        x = self.fc2(x)

        x = self.fc4(x)
        x = self.relu(x)
        x = self.fc2(x)

        x = x.view(-1, 3360)
        x = self.fc5(x)
        x = self.fc6(x)
        return x

device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
model = torch.load(path + 'model.pth')
model.to(device)
model.eval()
im = Image.open(image_file_name)
width, height = im.size
img = im.resize((64, 128))
img = np.array(img).transpose((2, 0, 1))
img = img / 255. - 0.5
img = torch.from_numpy(img).float()
input = Variable(img)
input = input.to(device)
output = model(input)
print(output)
# for x in range(width - 64):
#     for y in range(height - 128):
#         cropped = im.crop(x,y,x+64,y+128)
#         output = model(cropped)
        