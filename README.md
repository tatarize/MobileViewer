# Mobile Embroidery Viewer
View and tweak machine embroidery files on your Android device

Feature roadmap
===============

Version 1
---------
- Open DST file (using "File Storage" intent) (output: a populated Pattern class) -- Josh
- Render pattern on Canvas (custom view class) (input: a populated Pattern class)
- Show statistics (input: a populated Pattern class)
	- List of threads with associated colors and thread length used
	- Number of stitches by type (Normal, Jump, Stop)
	- Dimensions (width, height)

Version 2
---------
- Unknown: pick from enhancements

Enhancements (no particular order)
-------------------------------------------------
- Thumbnail directory viewer (generate and cache thumbnail view)
- Use "Android Instant Apps" (when available)
- Add ability to change thread colors for visualizing end result
- More realistic embroidery view
- Save any changes to DST format
- Add other embroidery formats as needed/requested

Questions
---------
- Use dependency injection? 
	- Classes: https://github.com/roboguice/roboguice
	- Views: http://jakewharton.github.io/butterknife/
- Automated builds
	- Travis??? : https://docs.travis-ci.com/user/languages/android
- UI testing
	- Espresso: https://google.github.io/android-testing-support-library/docs/espresso/
- Unit tests
	- Gradle?
