
class cell():
    def __init__(self, cols = 1, style = "unwork", content = ""):
        self.cols = cols
        self.style = style
        self.content = content


class day():
    def __init__(self, hour_cell, hhour_cell):
        self.hour = hour_cell

        self.hhour = []
        for cel in hhour_cell:
            if isinstance( cel, cell ):
                self.hhour.append( cel )
            else:
                self.hhour.append( cell( style = cel ) )


DAY_WORK_STA = {
    "2018" : [
        [], #1月
        [], #2月
        [], #3月
        [], #4月
        [], #5月
        [], #6月
        [], #7月
        [], #8月
        [], #9月
        [   #10月
            "unwork","unwork","unwork","unwork","unwork","unwork","unwork",
            "overtime", "overtime", "normal", "overtime", "normal", "unwork","unwork",
            "overtime", "overtime", "normal", "overtime", "normal", "unwork","unwork",
            "overtime", "overtime", "normal", "overtime", "normal", "dayon", "unwork",
            "overtime", "overtime", "normal",
        ], 
        [], #11月
        [], #12月
        ]
}

DAY_HOUR_STA = {
    "unwork" : day( [cell(2)], [cell(), cell()] ),
    "overtime" : day( [cell(2, "", i) for i in range(8, 21)], 
        ["unwork", "unwork", "unwork", "used", "used", "used", "used", "used", "unwork", "unwork",
        "unwork", "unwork", "used", "used", "used", "used", "used", "used", "used", "used",
        "unwork", "unwork", "used", "used", "used", "unwork"] ),
    "normal" : day( [cell(2, "", i) for i in range(8, 21)],
         ["unwork", "unwork", "unwork", "used", "used", "used", "used", "used", "unwork", "unwork",
        "unwork", "unwork", "used", "used", "used", "used", "used", "used", "used", "used",
        "unwork", "unwork", "unwork", "unwork", "unwork", "unwork"]),
    "dayon" : day( [cell(2, "", i) for i in range(8, 21)],
         ["used", "used", "unwork", "used", "used", "used", "used", "used", "unwork", "unwork",
        "unwork", "unwork", "used", "used", "used", "used", "used", "used", "used", "used",
        "unwork", "unwork", "unwork", "unwork", "unwork", "unwork"]),
}

    