files = [
    {
        "name":"plan",
        "status": "plan",
        "complete": 0,
    },
    {
        "name":"work_5",
        "status": "work",
        "complete": 5,
    },
    {
        "name":"work_10",
        "status": "work",
        "complete": 10,
    },
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
    with open(fc["name"], 'w') as f:
        complete = int(fc["complete"] / 5)
        ofst = 30 - complete
        height = complete
        sta_clr = status_color[fc["status"]]
        content = [
            '<?xml version="1.0" encoding="utf-8"?>',
            '<svg version="1.1" id="progress" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" ',
            '    width="10" height="30">',
            '<!-- global -->',
            '<rect x="0" y="0" width="10" height="30" stroke="none" fill="none" />',
            '<!-- progress -->',
            '<rect x="0" y="6" width="10" height="20" stroke="none" fill="{0}" />'.format(sta_clr),
            '<line x1="0" y1="6" x2="10" y2="6" stroke="black" />',
            '<line x1="0" y1="25" x2="10" y2="25" stroke="black" />',
            '<!-- complete -->',
            '<rect x="0" y="{0}" width="10" height="{1}" stroke="none" fill="black" />'.format(ofst,height),
            '</svg>',
        ]
        f.write(content)


     










