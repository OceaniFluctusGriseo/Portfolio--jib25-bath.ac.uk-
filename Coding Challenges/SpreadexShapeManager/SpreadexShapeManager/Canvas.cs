using SpreadexShapeManager.Shapes;
using System.ComponentModel.DataAnnotations;

namespace SpreadexShapeManager;

public sealed class Canvas
{
    [Required]
    private readonly List<Shape> _shapes = [];

    public void AddShape(Shape shape)
    {
        _shapes.Add(shape);
    }

    public void DescribeShapes()
    {
        _shapes.ForEach(shape => Console.WriteLine(shape.Describe()));
    }
}
