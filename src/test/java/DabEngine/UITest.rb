require 'DabEngine'

$container = DabEngine::UIContainer.new(150, 150, 300, 300);
button = DabEngine::UIButton.new(0, 0, 100, 100, "test");
button.setFont(DabEngine::ResourceManager::INSTANCE.getFont("C:/Windows/Fonts/arial.ttf", 24, 2))
button.setCallback { || puts "test" }
$container.addElement(button);

$container.observe($app.getInput.getMouse, $app.getInput.getKeyboard)

def draw(g)
    $container.draw(g);
end

def update
end