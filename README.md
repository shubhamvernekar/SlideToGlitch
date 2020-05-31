# SlideToGlitch
Android application to Glitch image. 
===
It corrupts images byte array so that they appear "glitched". The byte array can to obtained from Bitmap of an image.

<img src="https://github.com/shubhamvernekar/SlideToGlitch/blob/master/preview.gif" width="300" height="450" /><img src="https://github.com/shubhamvernekar/SlideToGlitch/blob/master/Optimized-Screenshot_2020-05-31-09-58-26-923_com.smv.slidetoglitch.jpg" width="300" height="450" /><img src="https://github.com/shubhamvernekar/SlideToGlitch/blob/master/Optimized-Screenshot_2020-05-31-10-01-43-805_com.smv.slidetoglitch(1).jpg" width="300" height="450" /> 

You can directly use this glitch effect in your project by copying Glitcher.java file.
The Glitcher.java file has static method glitch(), this method takes 4 parameters image bitmap, amount of glitch [1, 99], seed [0, 100], iterations [1, 100] & quality if image [1, 99]. and returns glitched image bitmap.

glitchedBitmap = Glitcher.glitch(Bitmap bitmap, int amount, int seed, int iterations, int quality);

#Improvements
===
If you're interested in adding more features, or want to countribute to project then following are the thing that can be **improved**.

- UI can be improved.
- Filtes can be added, such as RGB Glitch filters. (About tab can be replaced with filtes tab).
- Image share feature need to be added.
- The quality of glitched image can be improved.

#Importantly
===
This project was inspired from https://github.com/snorpey/jpg-glitch 

Thank you for visiting this repository.

#Try apk
===
Try this application [SlideToGlitch.apk](https://github.com/shubhamvernekar/SlideToGlitch/blob/master/SlideToGlitch.apk)
