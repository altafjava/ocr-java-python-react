import os
import cv2
import pytesseract
from pathlib import Path
pytesseract.pytesseract.tesseract_cmd = 'tesseract.exe'

path = Path(__file__).parent.as_posix()
inputFileName = path + '/input/image.png'
img = cv2.imread(inputFileName)
text = pytesseract.image_to_string(img)
outputDir = path+'/output'
outputFileName = 'output.txt'
dirName = outputDir
if(os.path.isdir(dirName) == False):
    os.makedirs(dirName)
output=outputDir+'/'+outputFileName
f = open(output, "w+")
f.write(text)
f.close()
print('Input =',inputFileName)
print('Output =',output)
print('========== IMAGE SUCCESSFULLY CONVERTED TO TEXT ==========')
