namespace SpreadexShapeManager.Shapes;

public abstract class Shape
{
    private readonly int _x;
    private readonly int _y;

    public Shape(int x, int y)
    {
        _x = x;
        _y = y;
    }

    public string Describe() => string.Join(" ", [GetName(), GetLocation(), GetDetails()]);

    private string GetName() => GetType().Name;

    private string GetLocation() => $"({_x},{_y})";

    protected abstract string GetDetails();
}
