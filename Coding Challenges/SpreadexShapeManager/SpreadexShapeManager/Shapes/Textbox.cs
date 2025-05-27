using System.ComponentModel.DataAnnotations;

namespace SpreadexShapeManager.Shapes;

public class Textbox : Rectangle
{
    [Required]
    private readonly string _text;

    public Textbox(int x, int y, int width, int height, string text) : base(x, y, width, height)
    {
        ArgumentNullException.ThrowIfNull(text);

        _text = text;
    }

    protected override string GetDetails() => base.GetDetails() + $" text=\"{_text}\"";
}
