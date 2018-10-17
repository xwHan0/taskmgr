
plan_status = ["plan{0}".format(i) for i in range(6)]
complete = list(range(0, 105, 5))

status_color = {
    "plan0" : "none",
    "plan1" : "#00FF00",
    "plan2" : "#FFFF00",
    "plan3" : "#FF8800",
    "plan4" : "#FF0000",
    "plan5" : "none",
}

SVG_WIDTH = 15

for p in plan_status:
    for c in complete:
        name = "resources/public/svg/progress_{0}_{1}.svg".format( p, c )
        f = open(name, 'w')
        comp = int(c / 5)
        ofst = 25 - comp
        height = comp
        sta_clr = status_color[p]
        content = [
            '<?xml version="1.0" encoding="utf-8"?>',
            '<svg version="1.1" id="progress" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" ',
            '    width="{0}" height="30">'.format( SVG_WIDTH ),

            '<!-- progress -->',
            '<rect x="0" y="6" width="{1}" height="20" stroke="none" fill="{0}" />'.format(sta_clr, SVG_WIDTH),
            '<line x1="0" y1="6" x2="{0}" y2="6" stroke="black" />'.format( SVG_WIDTH ),
            '<line x1="0" y1="25" x2="{0}" y2="25" stroke="black" />'.format( SVG_WIDTH ),

            '<!-- complete -->',
            '<rect x="0" y="{0}" width="{2}" height="{1}" stroke="none" fill="black" />'.format(ofst,height, SVG_WIDTH),
            '</svg>',
        ]
        f.writelines(content)
        f.close()




     










