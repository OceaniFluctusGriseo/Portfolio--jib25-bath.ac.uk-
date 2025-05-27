namespace SpreadexShapeManager.Shapes;

public class Circle : Ellipse
{
    public Circle(int x, int y, int diameter) : base(x, y, diameter, diameter) { }

    protected override string GetDetails() => $"size={Math.PI * Math.Pow(_horizontalDiameter / 2, 2)}";
}
