from fer import FER
import cv2
detector = FER()

while True:
    try:
        f = open('res.txt', 'w')
        emotion, score = detector.top_emotion(cv2.imread('out.jpg')) 
        f.write(emotion)
        f.close()
        #print(emotion)
    except:
        emotion = 'no face'
    
