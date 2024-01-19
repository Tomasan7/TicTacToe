class PlayerCardComponent extends HTMLElement
{
    constructor()
    {
        super()
        this.attachShadow({mode: 'open'})
        this.shadowRoot.appendChild(document.getElementById('player-card-template').content.cloneNode(true))
        this.symbolCanvas = this.shadowRoot.getElementById('symbol-canvas')
    }

    static get observedAttributes()
    {
        return ['name', 'symbol', 'color', 'symbolsize']
    }

    get name()
    {
        return this.getAttribute('name')
    }

    get symbol()
    {
        return this.getAttribute('symbol')
    }

    get color()
    {
        return this.getAttribute('color')
    }

    get symbolsize()
    {
        return this.getAttribute('symbolsize')
    }

    connectedCallback()
    {
        this.updateContent()
    }

    attributeChangedCallback(name, oldValue, newValue)
    {
        if (oldValue !== newValue)
        {
            this.updateContent()
            if (name === 'symbol' || name === 'color')
                this.updateSymbol()
        }
    }

    updateContent()
    {
        this.shadowRoot.getElementById('name').innerText = this.name
    }

    updateSymbol()
    {
        this.pixelCanvas = new PixelCanvas(this.symbolCanvas, this.symbolsize, this.symbolCanvas)
        this.pixelCanvas.clear()
        let imageData = symbolToImageData(this.symbol, this.color)
        this.pixelCanvas.context.putImageData(imageData, 0, 0)
    }
}

customElements.define('player-card', PlayerCardComponent)

function symbolToImageData(symbol, color)
{
    // Create imageData to set pixel values
    const imageData = this.context.createImageData(this.cellSize, this.cellSize);
    const pixelData = imageData.data;

    // Iterate through the symbol string and set pixel values
    for (let i = 0; i < symbol.length; i++)
    {
        const pixelIndex = i * 4; // Each pixel has 4 values (R, G, B, A)
        if (symbol[i] === '1')
        {
            pixelData[pixelIndex] = color[0];     // Red component
            pixelData[pixelIndex + 1] = color[1]; // Green component
            pixelData[pixelIndex + 2] = color[2]; // Blue component
            pixelData[pixelIndex + 3] = 255;      // Alpha component (fully opaque)
        } else
            pixelData[pixelIndex + 3] = 0;        // Alpha component (fully transparent)
    }

    return imageData;
}

class PixelCanvas
{
    constructor(canvas, width, height)
    {
        this.width = width;
        this.height = height;
        this.canvas = canvas;
        this.context = this.canvas.getContext('2d');
        this.context.imageSmoothingEnabled = false;
        this.context.webkitImageSmoothingEnabled = false;
        this.context.msImageSmoothingEnabled = false;
        this.canvas.style.imageRendering = 'pixelated';
        this.canvas.width = width;
        this.canvas.height = height;
    }

    setPixel(x, y, color)
    {
        const imageData = this.context.createImageData(1, 1);
        const pixelData = imageData.data;
        pixelData[0] = color[0];
        pixelData[1] = color[1];
        pixelData[2] = color[2];
        pixelData[3] = 255;
        this.context.putImageData(imageData, x, y);
    }

    clear()
    {
        this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);
    }
}

class GameCanvas extends PixelCanvas
{
    constructor(canvas, width, height, cellSize, dividerWidth)
    {
        super(canvas, width * cellSize + (width - 1) * dividerWidth, height * cellSize + (height - 1) * dividerWidth);

        this.width = width;
        this.height = height;
        this.cellSize = cellSize;
        this.dividerWidth = dividerWidth;

        // Draw the initial grid
        this.drawGrid();
    }

    drawGrid()
    {
        this.context.strokeStyle = '#000'; // Grid line color
        this.context.lineWidth = this.dividerWidth;

        for (let i = 1; i < this.width; i++)
        {
            const x = i * (this.cellSize + this.dividerWidth) - this.dividerWidth / 2;
            this.context.beginPath();
            this.context.moveTo(x, 0);
            this.context.lineTo(x, this.canvas.height);
            this.context.stroke();
        }

        for (let i = 1; i < this.height; i++)
        {
            const y = i * (this.cellSize + this.dividerWidth) - this.dividerWidth / 2;
            this.context.beginPath();
            this.context.moveTo(0, y);
            this.context.lineTo(this.canvas.width, y);
            this.context.stroke();
        }
    }

    drawSymbol(x, y, symbol, color)
    {
        let imageData = symbolToImageData(symbol, color);
        this.context.putImageData(imageData, x * (this.cellSize + this.dividerWidth), y * (this.cellSize + this.dividerWidth));
    }
}
