using SpreadexShapeManager;
using SpreadexShapeManager.Shapes;

namespace ShapeManagerTests;

public class ShapeManagerTests : IDisposable
{
    private readonly StringWriter _stringWriter;

    public ShapeManagerTests()
    {
        _stringWriter = new StringWriter();
        Console.SetOut(_stringWriter);
    }

    public void Dispose()
    {
        Console.SetOut(Console.Out);
        _stringWriter.Dispose();
    }

    private string CalculateCircleSize(int diameter)
    {
        return $"{Math.PI * Math.Pow(diameter / 2, 2)}";
    }

    #region Canvas Tests

    [Fact]
    public void CreateCanvas_NoShapes_NoOutput()
    {
        // Arrange
        var canvas = new Canvas();

        // Act 
        canvas.DescribeShapes();

        // Assert
        var output = _stringWriter.ToString();
        Assert.Empty(output);
    }

    [Fact]
    public void CreateCanvas_AddAllShapes_CorrectOutput()
    {
        // Arrange
        var canvas = new Canvas();

        var circle = new Circle(0, 0, 20);
        var circleOutput = "Circle (0,0) size=" + CalculateCircleSize(20);

        var ellipse = new Ellipse(10, -2, 10, 12);
        var ellipseOutput = "Ellipse (10,-2) diameterH=10 diameterV=12";

        var rectangle = new Rectangle(-10, 10, 20, 15);
        var rectangleOutput = "Rectangle (-10,10) width=20 height=15";

        var square = new Square(20, 30, 5);
        var squareOutput = "Square (20,30) size=25";

        var textbox = new Textbox(20, 40, 15, 10, "sample text");
        var textboxOutput = "Textbox (20,40) width=15 height=10 text=\"sample text\"";

        // Act
        canvas.AddShape(circle);
        canvas.AddShape(ellipse);
        canvas.AddShape(rectangle);
        canvas.AddShape(square);
        canvas.AddShape(textbox);

        canvas.DescribeShapes();

        // Assert
        var output = _stringWriter.ToString().Split("\r\n")[..^1];
        Assert.Equal(output[0], circleOutput);
        Assert.Equal(output[1], ellipseOutput);
        Assert.Equal(output[2], rectangleOutput);
        Assert.Equal(output[3], squareOutput);
        Assert.Equal(output[4], textboxOutput);
    }

    #endregion

    #region Circle Tests
    [Fact]
    public void CreateCircle_CorrectArguments_NoErrors()
    {
        // Arrange
        var x = 20;
        var y = 30;
        var diameter = 15;

        var description = "Circle (20,30) size=" + CalculateCircleSize(diameter);

        // Act and Assert
        try
        {
            var circle = new Circle(x, y, diameter);

            Assert.Equal(description, circle.Describe());
        }
        catch (Exception e)
        {
            Assert.Fail("Error was thrown when none expected: " + e.Message);
        }
    }

    [Fact]
    public void CreateCircle_NegativeDiameter_ThrowsError()
    {
        // Arrange
        var x = 10;
        var y = 20;
        var diameter = -5;

        // Act and Assert
        Assert.Throws<ArgumentException>(() => { var circle = new Circle(x, y, diameter); });
    }
    #endregion

    #region Ellipse Tests
    [Fact]
    public void CreateEllipse_CorrectArguments_NoErrors()
    {
        // Arrange
        var x = 12;
        var y = 14;
        var diameterH = 8;
        var diameterV = 20;

        var description = "Ellipse (12,14) diameterH=8 diameterV=20";

        // Act and Assert
        try
        {
            var ellipse = new Ellipse(x, y, diameterH, diameterV);

            Assert.Equal(description, ellipse.Describe());
        }
        catch (Exception e)
        {
            Assert.Fail("Error was thrown when none expected: " + e.Message);
        }
    }

    [Fact]
    public void CreateEllipse_NegativeDiameter_ThrowsError()
    {
        // Arrange
        var x = 12;
        var y = 14;

        var validDiameterH = 7;
        var invalidDiameterH = -7;

        var validDiameterV = 4;
        var invalidDiameterV = -4;

        // Act and Assert
        Assert.Throws<ArgumentException>(() => { var ellipse = new Ellipse(x, y, invalidDiameterH, validDiameterV); });
        Assert.Throws<ArgumentException>(() => { var ellipse = new Ellipse(x, y, validDiameterH, invalidDiameterV); });
    }
    #endregion

    #region Rectangle Tests
    [Fact]
    public void CreateRectangle_CorrectArguments_NoErrors()
    {
        // Arrange
        var x = -5;
        var y = 20;
        var width = 16;
        var height = 12;

        var description = "Rectangle (-5,20) width=16 height=12";

        // Act and Assert
        try
        {
            var rectangle = new Rectangle(x, y, width, height);

            Assert.Equal(description, rectangle.Describe());
        }
        catch (Exception e)
        {
            Assert.Fail("Error was thrown when none expected: " + e.Message);
        }
    }

    [Fact]
    public void CreateRectangle_NegativeLengths_ThrowsError()
    {
        // Arrange
        var x = -5;
        var y = 20;

        var validWidth = 45;
        var invalidWidth = -45;

        var validHeight = 23;
        var invalidHeight = -23;

        // Act and Assert
        Assert.Throws<ArgumentException>(() => { var rectangle = new Rectangle(x, y, invalidWidth, validHeight); });
        Assert.Throws<ArgumentException>(() => { var rectangle = new Rectangle(x, y, validWidth, invalidHeight); });
    }
    #endregion

    #region Square Tests
    [Fact]
    public void CreateSquare_CorrectArguments_NoErrors()
    {
        // Arrange
        var x = 80;
        var y = -100;
        var width = 42;

        var description = "Square (80,-100) size=1764";

        // Act and Assert
        try
        {
            var square = new Square(x, y, width);

            Assert.Equal(description, square.Describe());
        }
        catch (Exception e)
        {
            Assert.Fail("Error was thrown when none expected: " + e.Message);
        }
    }

    [Fact]
    public void CreateSquare_NegativeWidth_ThrowsError()
    {
        // Arrange
        var x = 80;
        var y = -100;

        var invalidWidth = -82;

        // Act and Assert
        Assert.Throws<ArgumentException>(() => { var square = new Square(x, y, invalidWidth); });
    }
    #endregion

    #region Textbox Tests
    [Fact]
    public void CreateTextbox_CorrectArguments_NoErrors()
    {
        // Arrange
        var x = -50;
        var y = -75;
        var width = 30;
        var height = 40;
        var text = "sample text";

        var description = "Textbox (-50,-75) width=30 height=40 text=\"sample text\"";

        // Act and Assert
        try
        {
            var textbox = new Textbox(x, y, width, height, text);

            Assert.Equal(description, textbox.Describe());
        }
        catch (Exception e)
        {
            Assert.Fail("Error was thrown when none expected: " + e.Message);
        }
    }

    [Fact]
    public void CreateTextbox_NegativeLengths_ThrowsError()
    {
        // Arrange
        var x = -50;
        var y = -75;

        var validWidth = 14;
        var invalidWidth = -14;

        var validHeight = 57;
        var invalidHeight = -57;

        var text = "sample text";

        // Act and Assert
        Assert.Throws<ArgumentException>(() => { var textbox = new Textbox(x, y, invalidWidth, validHeight, text); });
        Assert.Throws<ArgumentException>(() => { var textbox = new Textbox(x, y, validWidth, invalidHeight, text); });
    }

    [Fact]
    public void CreateTextbox_NullText_ThrowsError()
    {
        // Arrange
        var x = -50;
        var y = -75;
        var width = 30;
        var height = 40;

        // Act and Assert
        Assert.Throws<ArgumentNullException>(() => { var textbox = new Textbox(x, y, width, height, text: null); });
    }
    #endregion
}
