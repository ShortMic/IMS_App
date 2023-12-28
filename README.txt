Application: IMS Application. A javafx GUI Inventory Management System application to locally keep track of cupboard snack inventory stock with image classification/object detection automation capabilities as well as manual entry and modification functionality.
Author: Michael Short
Contact: msho110@wgu.edu
Version: 1.0
Date: 12/21/2023
IDE: Intellij Community 2021.3.3
Operating System: Windows 11 (tested on Windows 11, may be compatible with previous Windows OS versions and diffent OS's if programming languages and dependencies are installed accordingly for the currently used OS)
Program Language Packages: 
	Java: openjdk-18 - For the base interactive program (https://jdk.java.net/archive/)
	Python: python 3.10.6 (64-bit) - For the backend vision AI engine (https://www.python.org/downloads/release/python-3106/) update global environment path for AppData\Local\Programs\Python\Python310\ and AppData\Local\Programs\Python\Python310\Scripts or local target path if running through venv, docker or conda
Libraries and Frameworks:
	JavaFX: JavaFX-SDK-17.0.1 - for front end GUI display and control (https://gluonhq.com/products/javafx/)
	**Apache Commons CSV: commons-csv-1.10.0 - for csv file parsing and functionality in Java (https://commons.apache.org/proper/commons-csv/download_csv.cgi) 
	**Python Yolov5: YOLOv5 - Backend application that supplies API data to front end for machine learning AI functionality with image recognition/object detection and model training (https://github.com/ultralytics/yolov5) ***already downloaded into local app HOWEVER, user must install additional YOLOv5 python library dependencies themselves
	
Model:
	Custom trained model weights: last.pt based on YOLOv5s model
** DEPENDENCY ALREADY INSTALLED/DOWNLOADED LOCALLY IN APP, EXTERNAL DOWNLOAD/INSTALLATION NOT REQUIRED
Model data training framework and backend: Yolov5 (https://github.com/ultralytics/yolov5)
Image annotation and labeling: labelImg (https://github.com/HumanSignal/labelImg)

*** To install the yolov5 required dependencies, install python 3.10.6 and open bash or powershell terminal and change working directory to path/to/IMS_App/src/main/java/com/example/ims_app/yolov5 and then run command 'pip install -r requirements.txt'

Directions: Install all aforementioned languages, dependencies and frameworks. Run the IMSApplication.java class via wherever you installed the application root: path/to/IMS_App/src/main/java/com/example/ims_app/IMSApplication.java. Use the buttons for gui navigation and explore its functionality. Test the image recognition and object detection functionality by clicking the "Import from Image" button on the main application screen. By default, the text in the file nav address bar to the left should be a relative address that points to an untrained image sample to detect and extract data from and should automatically populate the product display table with its findings after a few seconds. You may additionally test this Import from Image feature on other images that you select in the browse button in the popup file explorer. As a prototype, the model is specifically trained for Diet Dr Pepper, Fruit Pear cups, Snack Size Original Pringles, and Rosarita Traditional Canned Refried Beans so results may vary (especially beyond that scope).