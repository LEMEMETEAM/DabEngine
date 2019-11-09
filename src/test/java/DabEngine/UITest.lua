require 'DabEngine.Script.dabengine'

container = dabengine.ui.new_container(150, 150, 300, 300)

button = dabengine.ui.new_button(0, 0, 100, 100, "test")
button:setFont(dabengine.resources.get_font("C:/Windows/Fonts/arial.ttf", 24, 2))
dabengine.ui.add_callback(button, function ()
    print("test")
end)

container:addElement(button)

container:observe(app:getInput():getMouse(), app:getInput():getKeyboard())

function draw(g)
    container:draw(g)
end

function update()
end