namespace SpreadexShapeManager.Shapes;

public class Ellipse : Shape
{
    protected readonly int _horizontalDiameter;

    protected readonly int _verticalDiameter;

    public Ellipse(int x, int y, int horizontalDiameter, int verticalDiameter) : base(x, y)
    {
        if (horizontalDiameter < 0 || verticalDiameter < 0)
        {
            throw new ArgumentException("Measurements must be non-negative");
        }

        _horizontalDiameter = horizontalDiameter;
        _verticalDiameter = verticalDiameter;
    }

    protected override string GetDetails() => $"diameterH={_horizontalDiameter} diameterV={_verticalDiameter}";
}
