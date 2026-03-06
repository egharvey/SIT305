# 2.1P - Travel Buddy Unit Converter
This is an android app that can convert between currencies, tempretures as well as fuel efficiency and distances.  
With a simple UI, select the units you wish to convert to and from in the relevent dropdowns and then enter the from value and press convert to get your conversion.  
  
Once you have pulled the repository, you can find:
- UI design in <2.1P/app/src/main/res/layout/activity_main.xml>
- The main code in <2.1P/app/src/main/java/com/example/unitconverter/MainActivity.java>
- In <2.1P/app/src/main/res/values/themes.xml>
    - colors.xml features colours used in the UI design of the app.
    - strings.xml contains strings used in the UI, including the arrays at the source of the dropdown spinners.
  
## Current Issues
- App will crash if user clicks convert without entering a number to convert from.
- UI is limited and may not be the most user friendly.
    - Could use better colour combinations.
    - Could use subheadings for each converter.
- Fuel efficiency and distance converter was required to be one conversion in the tasksheet, but due to me not understanding what was meant by this and any attempt to seek clarification going unanswered, currently this converter will only convert between units of the same type.
