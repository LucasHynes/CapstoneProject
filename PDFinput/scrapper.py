import cv2
import pytesseract
from pdf2image import convert_from_path

def ocr_core(img):
    text = pytesseract.image_to_string(img)
    return text

def thresholdings(img):
    return cv2.threshold(img, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)[1]

path = "PDFinput\\20210412062708295.pdf"

images = convert_from_path(path)
for i in range(len(images)):
    images[i].save("page" + str(i) + path.split('\\')[1].split('.')[0] + ".jpg", "JPEG")
    img = cv2.imread("page" + str(i) + path.split('\\')[1].split('.')[0] + ".jpg")
    img = thresholdings(img)
    print(ocr_core(img))

