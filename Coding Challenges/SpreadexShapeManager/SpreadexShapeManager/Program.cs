using SpreadexShapeManager.Shapes;

namespace SpreadexShapeManager;

class Program
{
    static void Main(string[] args)
    {
        var canvas = new Canvas();

        var rect = new Rectangle(x: 10, y: 10, width: 30, height: 40);
        var square = new Square(x: 15, y: 30, width: 6);
        var ellipse = new Ellipse(x: 100, y: 150, horizontalDiameter: 300, verticalDiameter: 200);
        var circle = new Circle(x: 1, y: 1, diameter: 20);
        var textBlock = new Textbox(x: 5, y: 5, width: 200, height: 100, text: "sample text");

        canvas.AddShape(rect);
        canvas.AddShape(square);
        canvas.AddShape(ellipse);
        canvas.AddShape(circle);
        canvas.AddShape(textBlock);

        canvas.DescribeShapes();
    }
}