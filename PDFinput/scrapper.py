
import os
from pip._vendor import requests
URL = "https://www.i2ocr.com/pdf-ocr"
page = requests.get(URL)
print(page)