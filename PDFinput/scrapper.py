import cv2
import pytesseract
from pdf2image import convert_from_path

#used to compile the final text
def ocr_core(img):
    text = pytesseract.image_to_string(img)
    return text

#defines threshold of the pdf
def thresholdings(img):
    return cv2.threshold(img, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)[1]

#defines the path for the testing pdf
path = "PDFinput\\20210412062708295.pdf"


#converts the image from pdf to jpeg
images = convert_from_path(path)

#loops through pages, saving images then using pytesseract to get the information from
#the document.
for i in range(len(images)):
    images[i].save("page" + str(i) + path.split('\\')[1].split('.')[0] + ".jpg", "JPEG")
    img = cv2.imread("page" + str(i) + path.split('\\')[1].split('.')[0] + ".jpg")
    img = thresholdings(img)
    print(ocr_core(img))

