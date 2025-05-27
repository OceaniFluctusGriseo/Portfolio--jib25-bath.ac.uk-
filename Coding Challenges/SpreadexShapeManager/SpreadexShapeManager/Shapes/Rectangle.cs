using System.ComponentModel.DataAnnotations;

namespace SpreadexShapeManager.Shapes;

public class Rectangle : Shape
{
    [Required]
    protected readonly int _width;

    [Required]
    protected readonly int _height;

    public Rectangle(int x, int y, int width, int height) : base(x, y)
    {
        if (width < 0 || height < 0)
        {
            throw new ArgumentException("Measurements must be non-negative");
        }

        _width = width;
        _height = height;
    }

    protected override string GetDetails() => $"width={_width} height={_height}";
}
