from datetime import *

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

DAY_HOUR_STA = {
    "unwork" : day( [cell(2)], [cell(), cell()] ),
    "overtime" : day( [cell(2, "", i) for i in range(8, 21)], 
        ["unwork", "unwork", "unwork", "used", "used", "used", "used", "used", "unwork", "unwork",
        "unwork", "unwork", "used", "used", "used", "used", "used", "used", "used", "used",
        "unused", "unused", "used", "used", "used", "unwork"] ),
    "normal" : day( [cell(2, "", i) for i in range(8, 21)],
         ["unwork", "unwork", "unwork", "used", "used", "used", "used", "used", "unwork", "unwork",
        "unwork", "unwork", "used", "used", "used", "used", "used", "used", "used", "used",
        "unwork", "unwork", "unwork", "unwork", "unwork", "unwork"]),
    "dayon" : day( [cell(2, "", i) for i in range(8, 21)],
         ["used", "used", "unwork", "used", "used", "used", "used", "used", "unwork", "unwork",
        "unwork", "unwork", "used", "used", "used", "used", "used", "used", "used", "used",
        "unwork", "unwork", "unwork", "unwork", "unwork", "unwork"]),
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

        self.start = day
        self.finish = (day+timedelta(days=21)).replace(hour=23, minute=59)

        day1 = timedelta(days = 1)
        for i in range(21):
            work_sta = day_work_sta[str(day.year)][day.month-1][day.day-1]

            self.day_header_hour.append( {"day":day.day, "style":work_sta} )

            day_style = DAY_HOUR_STA[work_sta]

            # 天表头
            self.day_header_day_html += '<th class="{0}" colspan="{1}">{2}</th>\n'.format(work_sta, len(day_style.hhour), day.day)

            # 小时表头
            l = [' <th class="{0}" colspan="{1}">{2}</th>\n'.format(h.style, h.cols, h.content) for h in day_style.hour]
            self.day_header_hour_html += "".join( l )

            # 半小时表头
            l = [' <th class="{0}" colspan="{1}">{2}</th>\n'.format(h.style, h.cols, "&nbsp;") for h in day_style.hhour]
            self.day_header_hhour_html += "".join( l )

            day = day + day1

    def hour_header(self, from_date, to_date, ishour = True):

        rst = ""
        day1 = timedelta(days = 1)
        day = from_date

        while day <= to_date:

            work_sta = day_work_sta[str(day.year)][day.month-1][day.day-1]
            day_style = DAY_HOUR_STA[work_sta]
            
            if ishour:
                rst += "".join( [' <th class="{0}" colspan="{1}">{2}</th>\n'.format(h.style, h.cols, h.content) for h in day_style.hour] )
            else:
                rst += "".join( [' <th class="{0}" colspan="{1}">{2}</th>\n'.format(h.style, h.cols, h.content) for h in day_style.hhour] )

            # 递增日期
            day = day + day1

        return rst


# gnt = gantt()       