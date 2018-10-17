import sqlite3

from gantt.const import *


class task():

    def __init__(self, name, owner = None, description = None, start = None, finish = None):
        self.name = name
        self.owner owner
        self.description = desription
        self.start = start
        self.finish = finish
        self.info = []
        self.tid = -1
    
    def read_detail(self):
        conn = sqlite3.connect('../resources/database/tmgr.sqlite')
        c = conn.execute('SELECT * FROM information WHERE tid=?', (self.tid,))
        self.info = c.fetchall()
        c.close()
        
    def read_information(self):
        conn = sqlite3.connect('../resources/database/tmgr.sqlite')
        c = conn.execute('SELECT * FROM information WHERE tid=?', (self.tid,))
        self.info = c.fetchall()
        c.close()
        
    def detail(self, owner, description, start, finish):
        self.owner owner
        self.description = desription
        self.start = start
        self.finish = finish
        
    def in_info(self, day, hhour, st, ed):
    
        hhour += 16
        if hhour % 2 == 1:
            hhour = day.replace(hour=hhour//2, minute=30, second=0)
        else:
            hhour = day.replace(hour=hhour//2, minute=0, second=0)
    
        if st.minute < 30:
            st = st.replace(minute=0, second=0)
        else:
            st = st.replace(minute=30, second=0)
            
        if ed.minute <= 30:
            ed = ed.replace(minute=30, second=59)
            last = ed.replace(minute=0)
        else:
            ed = ed.replace(minute=59, second=59)
            last = ed.replace(minute=30)
            
        if hhour == last: return 2
        elif hhour >= st and hhour <= ed: return 1
        else: return 0
        
    def html_task(self):
        return '<td>{0}</td><td>{1}</td><td>{2}</td><td>{3}</td>'.format(self.name, self.owner, self.start, self.finish)

    def html_hhour(self, start, finish):
        rst = ""
        
        start = start.replace(hour=0, minute=0, second=0)
        finish = finish.replace(hour=23, minute=59, second=59)
        status = "START" if self.start < start else "IDLE"
        
        day = start
        info_idx = 0
        while day <= finish:
            day_sta = DAY_WORK_STA[day.year]
            if day_sta == "unwork":
                rst += '<td class="unwork"></td><td class="unwork"></td>'
            else:
                for hhour, hhour_st in enumerate(DAY_HOUR_STA[day_sta].hhour):
                    if hhour_st == "unwork":
                        rst += '<td class="unwork"></td>'
                    else:
        
        