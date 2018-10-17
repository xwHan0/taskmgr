import sqlite3
from datetime import *
from gantt.const import *

def datetime_union(d):
    """"""
    if d.minute < 30:
        return d.replace(minute=0, second=0)
    else:
        return d.replace(minute=30, second=0)


class Information:
    def __init__(self, id = 1, type = "info", owner = "hanxinwei", description = "", start = None, finish = None, status = "open", complete = 0):
        self.id = id
        self.type = type
        self.owner = owner
        self.description = description
        self.start = datetim_union( start )
        self.finish = datetime_union( finish )
        self.status = status
        self.complete = complete


class Task:

    def __init__(self, name, owner = None, description = None, start = None, finish = None):
        self.name = name
        self.start = start
        self.finish = finish
        self.info = []
        self.plan = []
        self.tid = -1
    
    def read_detail(self):
        conn = sqlite3.connect('../resources/database/tmgr.sqlite')
        conn.row_factory = sqlite3.Row
        c = conn.execute('SELECT * FROM information WHERE tid=?', (self.tid,))
        self.info = c.fetchall()
        c.close()
        
    def read_information(self, typ):
        conn = sqlite3.connect('../resources/database/tmgr.sqlite', detect_types=sqlite3.PARSE_DECLTYPES|sqlite3.PARSE_COLNAMES)
        conn.row_factory = sqlite3.Row
        c = conn.execute('SELECT * FROM information WHERE tid=? AND type=? ORDER BY finish ASC', (self.tid, typ))
        #rst = c.fetchall()
        rst = [Information(id=info["id"], type=info["type"], owner=info["owner"], description=info["description"], start=info["start"], finish=info["finish"], status=info["status"], complete=info["complete"]) for info in c]
        c.close()
        return rst
        
    def read_plan(self):
        self.plan = self.read_information("plan")
        
    def read_info(self):
        self.info = self.read_information("info")
        
    def hhour_datetime(self, hhour, day):
        """"""
        hhour += 16
        if hhour % 2 == 1:
            return day.replace(hour=hhour//2, minute=30, second=0)
        else:
            return day.replace(hour=hhour//2, minute=0, second=0)
     
    def plan_status(self, hhour):
        """"""
        if self.plan == []: return "plan0"
        if hhour < self.plan[0].start: return "plan0"
        for i,v in enumerate(self.plan):
            if hhour < v.finish:
                break
        return "plan{0}".format(i+1)
        
    def info_status(self, hhour, idx):
        if idx >= len(self.info): return (None, False)
        
        info = self.info[idx]
        if hhour == info.finish: return (info, True)
        elif hhour >= info.start and hhour <= info.finish: return (info, False)
        else: return (None, False)
        
    def html_task(self):
        return '<td>{0}</td><td>{1}</td><td>{2}</td><td>{3}</td>'.format(self.name, self.owner, self.start, self.finish)

    def html_hhour(self, start, finish):
        rst = ""
        
        start = datetime_union(start)
        finish = datetime_union(finish)
         
        day = start
        day1 = timedelta(days=1)
        info_idx = 0
        plan_status = "plan0"
        td = '<td class="{0}"><img src="{{url_for(\"static\", filename=\"svg/progress_{1}_{2}.svg\")}}"/></td>\n'
                    
        while day <= finish:
            day_sta = DAY_WORK_STA[str(day.year)][day.month-1][day.day-1]
            
            if day_sta == "unwork":
                rst += td.format(day_work, plan_status, 0)
                continue
            
            for hhour, hhour_st in enumerate(DAY_HOUR_STA[day_sta].hhour):
                if hhour_st.style == "unwork":
                    rst += td.format("unwork", plan_status, 0)
                    continue
                hhour = self.hhour_datetime(hhour, day)
                plan_status = self.plan_status(hhour)
                info, nxt = self.info_status(hhour, info_idx)
                if nxt: info_idx += 1
        
                if info == None: 
                    rst += td.format(hhour_st.style, plan_status, 0)
                else:
                    rst += td.format(hhour_st.style, plan_status, complete)
                    
            day += day1
            
        return rst
        
        