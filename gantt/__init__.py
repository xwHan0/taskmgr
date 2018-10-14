from datetime import *

day_work_sta = {
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

day_header_html = {
    "hour" : "".join( ['<th colspan="2" class="">{0}</th>\n'.format(i) for i in range(8,21)] ),
    "overtime" : ["unused", "unused", "unused", "used", "used", "used", "used", "used", "unused", "unused",
        "unused", "unused", "used", "used", "used", "used", "used", "used", "used", "used",
        "unused", "unused", "used", "used", "used", "unused"],
    "normal" : ["unused", "unused", "unused", "used", "used", "used", "used", "used", "unused", "unused",
        "unused", "unused", "used", "used", "used", "used", "used", "used", "used", "used",
        "unused", "unused", "unused", "unused", "unused", "unused"],
    "dayon" : ["used", "used", "unused", "used", "used", "used", "used", "used", "unused", "unused",
        "unused", "unused", "used", "used", "used", "used", "used", "used", "used", "used",
        "unused", "unused", "unused", "unused", "unused", "unused"]
}

class gantt():

    def __init__(self, t=None):

        self.day_header_hour = []
        self.day_header_day_html = ""
        self.day_header_hour_html = ""
        self.day_header_hhour_html = ""

        t = t if t else datetime.today()    # 获取日期
        day_ofst = timedelta(days = 7 + t.weekday())    # 计算上一个星期一距给定日期t的天数差
        day = t - day_ofst   # 计算上一个星期一的日期

        day1 = timedelta(days = 1)
        for i in range(21):
            work_sta = day_work_sta[str(day.year)][day.month-1][day.day-1]

            self.day_header_hour.append( {"day":day.day, "style":work_sta} )

            if work_sta == "unwork":
                self.day_header_day_html += '<th class="{0}">{1}</th>\n'.format(work_sta, day.day)
                self.day_header_hour_html += '<th class="unwork"></th>\n'
                self.day_header_hhour_html += '<th class="unwork"></th>\n'
            else:
                self.day_header_day_html += '<th class="{0}" colspan="26">{1}</th>\n'.format(work_sta, day.day)
                self.day_header_hour_html += day_header_html['hour']
                for style in day_header_html[work_sta]:
                    self.day_header_hhour_html += '<th class="{0}"></th>\n'.format(style)
            day = day + day1

gnt = gantt()       