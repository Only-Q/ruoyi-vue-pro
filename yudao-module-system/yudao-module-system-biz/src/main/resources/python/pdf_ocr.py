import io
import sys
sys.stdout = io.TextIOWrapper(sys.stdout.buffer,encoding='GB18030')
import cv2
import fitz
import numpy as np
from paddleocr import PaddleOCR
pOCR = PaddleOCR(type='STRUCTURE',use_angle_cls=True,lang='ch')
mat = fitz.Matrix(2.0,2.0)

def pdf_ocr(url):
    try:
        doc = fitz.open(url)
    except Exception as E:
        print(E)
    for i in range(0,doc.pageCount):
        page = doc[i]
        pix = page.get_pixmap(colorspace=fitz.csGRAY,matrix=mat)
        image = pix.tobytes("png")

        np_arr = np.frombuffer(image, dtype=np.uint8)
        img = cv2.imdecode(np_arr, cv2.IMREAD_COLOR)
        rst = pOCR(img)
        print("page " + str(i + 1))
        for e in rst[1]:
            print(e[0])


pdfPath = sys.argv[1]
pdf_ocr(pdfPath)
