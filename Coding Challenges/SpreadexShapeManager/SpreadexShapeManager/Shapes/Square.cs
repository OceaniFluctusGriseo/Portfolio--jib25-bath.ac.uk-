namespace SpreadexShapeManager.Shapes;

public class Square : Rectangle
{
    public Square(int x, int y, int width) : base(x, y, width, width) { }

    protected override string GetDetails() => $"size={_width * _width}";
}
