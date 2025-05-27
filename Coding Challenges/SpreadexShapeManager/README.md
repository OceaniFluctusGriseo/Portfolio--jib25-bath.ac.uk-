# Spreadex Coding Exercise
## Table of Contents
- [Introduction](#Introduction)
- [Design-Choices](#Design-Choices)
- [Future-Improvements](#Future-Improvements)
- [Disclaimers](#Disclaimers)

## Introduction
This file will detail the code I produced for the spreadex Shape Manager coding exercise.

To run the tests for this project you may need to install Xunit.

## Design Choices
#### Representing Shapes
My first train of thought for this exercise was how to represent each type of shape and the common behaviour between them all that can be abstracted into an abstract class or an interface.

I realised a common feature between all shapes was coordinates, each has their own with the same behaviour. The differences began where other properties were involved; for example, Circle has just a diameter whereas Rectangle has a width and height. Each shape may have its own unique property, however each needed to be able to print out each of its properties regardless.

#### Abstract Base Class
Therefore, I decided to implement an abstract class 'Shape' that contained both X and Y coordinates as properties. As well as this, each class had some shared information in its outputted description: class name, coordinates and all other properties.

All shapes can share a function to get its class name and likewise coordinates as part of the base class. However, as properties vary per shape type, I created an abstract method to print a shape's properties that the base's children must implement.

#### Inheritance Structure
Utilising my Shape base class, I could implement the five required shapes: circle, ellipse, square, rectangle and textbox. To begin with, I created Rectangle and Ellipse which implement Shape. 

In my head I connected Circle and Ellipse, a circle being an ellipse with equal horizontal and vertical diameter. Due to this, I made Circle a child of Ellipse. In the future, I can use this inheritance structure to utilise mathematical similarities between the shape.

In a similar vein, I had Square and Textbox inheriting from Rectangle. This is because a Square is a Rectangle with equal width and height. Similarly, the only difference between Textbox and Rectangle was the Text property it held inside.

I believe my approach has benefits where future mathematical operations can utilise this inheritance structure, such as calculating area or perimeter.

#### Canvas Class
Finally, I needed some encapsulation for all the shapes that would be hardcoded into our program. Thus I made a simple 'Canvas' class to store shapes in a list and print out their details in the order the shapes are applied to the Canvas.

This class could also be extended in the future to provide bulk operations on shapes within the Canvas, such as translation.

#### Final Thoughts
The approach I chose very much leaves the classes open for extension in my opinion. If all shapes require a new shared functionality, it can be added to the abstract base class.

I also believe each class having its own unique Description function grants them single responsibility. If the requirements or properties required for each shape changes, only that class needs updating.

## Future Improvements
#### Interface Abstraction
Another method I considered for my base class was instead making it an interface, for example 'IShape' that contained the virtual methods for getting the description of a shape, and other behaviour a type of shape may need.

However I was conflicted because interfaces are not allowed to implement properties, and as each shape - regardless of type - required an X and Y coordinate, I opted for an abstract class instead. On the other hand, perhaps I could have used both the abstract class for the coordinates and an interface for the description method. Such an interface could be called 'IDescribable'.

#### Reflection to Access Properties
While I was able to use polymorphism to achieve printing the unique properties of each type of shape, I couldn't help but think there was a better way that could be abstracted so I need not define it for each type of shape. Moreover, I was not too happy with defining a hard-coded string output for each type.

A thought I had was to use reflection to output each class' individual properties and join them by a delimiter to achieve the same desired output. However, this approach would not work with my inheritance structure as not all parent properties of a shape may be desired in its properties and I do not believe reflection would allow me to avoid this without even further work.

#### Data Annotations
One part of this exercise was ensuring measurements are non-negative. I achieve this by checking in constructors but if I had more time I may have implemented a cleaner data annotation to mark an integer property as requiring a non-negative value.

## Disclaimers
The output of my program does not exactly match the output desired as stated in the specification document. I believe I may have misunderstood some of the terms used, for example 'size' on the Square and Circle shapes. 

My train of thought was that 'size' referred to area as in the previous description a Circle was defined as having only diameter and a Square only width. I therefore believed 'size' was not the same as these single properties but the physical area a shape would occupy.

Due to this, I could not get exactly the desired 'size' for my Circle and Square shapes because, as the measurements must be integers, there is no integer for which the diameter of a Circle gives area 300 units^2. Likewise, there is no integer width of a square which gives an area of 35 units^2.

Please forgive me any misunderstanding on this part.