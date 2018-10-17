files = [
    {"plan_status":"plan0", "complete": 0,},
    
]

status_color = {
    "plan0" : "none",
    "plan1" : "green",
    "plan2" : "#880000",
    "plan3" : "#CC0000",
    "plan4" : "#FF0000",
}

for fc in files:

    svg_width = 15      # SVG image width

    name = "resources/public/svg/progress_" + fc["plan_status"] + fc["complete"] + ".svg"
    f = open(name, 'w')
    complete = int(fc["complete"] / 5)
    ofst = 25 - complete
    height = complete
    sta_clr = status_color[fc["plan_status"]]
    content = [
        '<?xml version="1.0" encoding="utf-8"?>',
        '<svg version="1.1" id="progress" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" ',
        '    width="{0}" height="30">'.format( svg_width ),

        '<!-- progress -->',
        '<rect x="0" y="6" width="{1}" height="20" stroke="none" fill="{0}" />'.format(sta_clr, svg_width),
        '<line x1="0" y1="6" x2="{0}" y2="6" stroke="black" />'.format( svg_width ),
        '<line x1="0" y1="25" x2="{0}" y2="25" stroke="black" />'.format( svg_width ),

        '<!-- complete -->',
        '<rect x="0" y="{0}" width="{2}" height="{1}" stroke="none" fill="black" />'.format(ofst,height, svg_width),
        '</svg>',
    ]
    f.writelines(content)
    f.close()


     










