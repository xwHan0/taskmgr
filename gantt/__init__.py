from datetime import *
from gantt.const import *



class Gantt():

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
            work_sta = DAY_WORK_STA[str(day.year)][day.month-1][day.day-1]

            self.day_header_hour.append( {"day":day.day, "style":work_sta} )

            day_style = DAY_HOUR_STA[work_sta]

            # 天表头
            self.day_header_day_html += '<th class="{0}" colspan="{1}">{2}</th>\n'.format(work_sta, len(day_style.hhour), day.day)

            # 小时表头
            l = [' <th class="{0}" colspan="{1}">{2}</th>\n'.format(h.style, h.cols, h.content) for h in day_style.hour]
            self.day_header_hour_html += "".join( l )

            # 半小时表头
            l = [' <th class="{0}" colspan="{1}"><div>{2}</div></th>\n'.format(h.style, h.cols, "&nbsp;") for h in day_style.hhour]
            self.day_header_hhour_html += "".join( l )

            day = day + day1

    def hour_header(self, from_date, to_date, ishour = True):

        rst = ""
        day1 = timedelta(days = 1)
        day = from_date

        while day <= to_date:

            work_sta = DAY_WORK_STA[str(day.year)][day.month-1][day.day-1]
            day_style = DAY_HOUR_STA[work_sta]
            
            if ishour:
                rst += "".join( [' <th class="{0}" colspan="{1}">{2}</th>\n'.format(h.style, h.cols, h.content) for h in day_style.hour] )
            else:
                rst += "".join( [' <th class="{0}" colspan="{1}">{2}</th>\n'.format(h.style, h.cols, h.content) for h in day_style.hhour] )

            # 递增日期
            day = day + day1

        return rst


# gnt = gantt()       