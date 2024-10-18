Augmentive and Alternative Communication Devices
==================

A CSC-207 project on augmentive and alternative communication devices.

**Author**
Natalie Nardone

CSC-207-01

October 17, 2024

GitHub Repository: <https://github.com/natnardone/AACNestedHW>

Acknowledgements:

Java Documentation for File, Scanner, and FileWriter for the AACMappings constructor and writeToFile method.


Note on Tests:

My code fails the tests `testSelectExceptional()` and `testAdd()` because these tests are not consistent with the AACMappings documentation for `select` at <https://accessibilityeducation.github.io/assignments/AAC/AACMappings.html>. The documentation states that "If the image provided is a category, it updates the AAC's current category to be the category associated with that image and returns the empty string. If the AAC is currently in a category and the image provided is in that category, it returns the text to be spoken." However, `testSelectExceptional()` requires that an exception is thrown if a category name is selected when already in that category, and that "A different category name shouldn't work within a category", neither of which are required by the documentation. Additionally, the documentation does not state what `select` should do if a category and an image in a category share the same name, but `testAdd()` requires that it choose the image in the category versus the category itself.