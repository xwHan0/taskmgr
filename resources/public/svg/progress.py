files = [
    {"name":"plan", "status": "plan", "complete": 0,},
    
    {"name":"work_5","status": "work","complete": 5,},
    {"name":"work_10","status": "work","complete": 10,},
    {"name":"work_15","status": "work","complete": 15,},
    {"name":"work_20","status": "work","complete": 20,},
    {"name":"work_25","status": "work","complete": 25,},
    {"name":"work_30","status": "work","complete": 30,},
    {"name":"work_35","status": "work","complete": 35,},
    {"name":"work_40","status": "work","complete": 40,},
    {"name":"work_45","status": "work","complete": 45,},
    {"name":"work_50","status": "work","complete": 50,},
    {"name":"work_55","status": "work","complete": 55,},
    {"name":"work_60","status": "work","complete": 60,},
    {"name":"work_65","status": "work","complete": 65,},
    {"name":"work_70","status": "work","complete": 70,},
    {"name":"work_75","status": "work","complete": 75,},
    {"name":"work_80","status": "work","complete": 80,},
    {"name":"work_85","status": "work","complete": 85,},
    {"name":"work_90","status": "work","complete": 90,},
    {"name":"work_95","status": "work","complete": 95,},
    {"name":"work_100","status": "work","complete": 100,},
]

status_color = {
    "plan" : "none",
    "work" : "green",
    "exception" : "blue",
    "delay1" : "#880000",
    "delay2" : "#CC0000",
    "delay3" : "#FF0000",
}

for fc in files:

    svg_width = 15      # SVG image width

    name = "resources/public/svg/progress_" + fc["name"] + ".svg"
    f = open(name, 'w')
    complete = int(fc["complete"] / 5)
    ofst = 25 - complete
    height = complete
    sta_clr = status_color[fc["status"]]
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


     










