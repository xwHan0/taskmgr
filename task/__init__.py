import sqlite3
from datetime import *
from gantt.const import *

def addTask(title, pid = 0):
    conn = sqlite3.connect('resources/database/tmgr.sqlite')
    c = conn.cursor()
    c.execute('INSERT INTO task (pid, title) VALUES (?,?)', (pid, title))
    conn.commit()
    conn.close()

def datetime_union(d):
    """归一化时间到'半小时'的起始位置。"""
    if d.minute < 30:
        return d.replace(minute=0, second=0, microsecond=0)
    else:
        return d.replace(minute=30, second=0, microsecond=0)


class Information:
    def __init__(self, id = 1, type = "info", owner = "hanxinwei", description = "", start = None, finish = None, status = "open", complete = 0):
        self.id = id
        self.type = type
        self.owner = owner
        self.description = description
        self.start = datetime_union( start )
        self.finish = datetime_union( finish )
        self.status = status
        self.complete = complete


class Task:

    def __init__(self, id = 0, pid = 0, title = "", type = "", style = ""):
        
        self.id = id
        self.pid = pid
        self.title = title
        self.type = type
        self.style = style

        self.info = []
        self.plan = []
        self.sub = []

    def read_tasks(self, id = None):

        # 定义内部迭代函数
        def read_iter( this, conn ):
            # 读当前任务信息
            c = conn.execute('SELECT * FROM task WHERE id=?', (this.id,)).fetchone()
            this.pid, this.title, this.type, this.style = c['pid'], c['title'], c['class'], c['style']

            # 读子任务信息
            c = conn.execute('SELECT * FROM task WHERE pid=?', (this.id, ))
            this.sub = [Task( t['id'], t['pid'], t['title'], t['class'], t['style'] ) for t in c]

            # 迭代回归子任务
            for s in this.sub:
                read_iter( s, conn )

        # 重新赋值ID
        self.id = id if id else self.id

        # 打开数据库
        conn = sqlite3.connect('resources/database/tmgr.sqlite')
        conn.row_factory = sqlite3.Row
        
        # 回归读取
        read_iter( self, conn )

        # 关闭数据库
        conn.close()
    
    def read_information(self, typ):
        conn = sqlite3.connect('resources/database/tmgr.sqlite', detect_types=sqlite3.PARSE_DECLTYPES|sqlite3.PARSE_COLNAMES)
        conn.row_factory = sqlite3.Row
        c = conn.execute('SELECT * FROM information WHERE tid=? AND type=? ORDER BY finish ASC', (self.id, typ))
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
            return day.replace(hour=hhour//2, minute=30, second=0, microsecond=0)
        else:
            return day.replace(hour=hhour//2, minute=0, second=0, microsecond=0)
     
    def plan_status(self, hhour):
        """返回hhour在任务计划中的位置(状态)序号。"""
        if self.plan == []: return 0
        if hhour < self.plan[0].start: return 0
        if hhour > self.plan[-1].finish: return -1  # 任务已经完成状态
        for i,v in enumerate(self.plan):
            if hhour < v.finish:
                return i+1
        return 0
        
    def info_status(self, hhour, idx):
        if idx >= len(self.info): return (None, False)
        
        info = self.info[idx]
        if hhour == info.finish: return (info, True)
        elif hhour >= info.start and hhour <= info.finish: return (info, False)
        else: return (None, False)
        
    def html_sub_task(self, lvl_str = 'Task0'):
        for t in self.sub:
            html += '<tr id="{0}">\n'.format(lvl_str)
            html += '    <th class="field">{0}</th>'.format(self.title)
            html += '    <th class="field">{0}</th>'.format(self.info[-1].owner)
            html += '    <th class="field">{0}</th>'.format(self.info[-1].status)
            html += '    <th class="field">{0}</th>'.format(self.info[-1].finish)
            html += '</tr>\n'
            # 深度优先递归添加
            html += t.html_sub_task( lvl_str + '_0' )
        return html

    def html_td(self, hhour_status, plan_status, complete, info_id = 0):
        if plan_status == 0 and complete == 0:  # 没有开始/已经结束的任务，并且没有info信息，则跳过
            return '<td class="{0}"></td>\n'.format(hhour_status)
        else:
            return """<td class="{0}"><a href="javascript:void(0)"><img id="info{3}" src="/public/svg/progress_plan{1}_{2}.svg"/></a></td>\n""".format(hhour_status, plan_status, complete, info_id)
        
    def html_hhour(self, start, finish):
        rst = ""
        
        start = datetime_union(start)
        finish = datetime_union(finish)
         
        day = start     # 循环起始天
        day1 = timedelta(days=1)    # 按天循环
        info_idx = 0    # 读取info序号
        plan_status = 0 # 起始任务计划状态序号
                    
        while day <= finish:
            day_sta = DAY_WORK_STA[str(day.year)][day.month-1][day.day-1]   # 获取当前'天'工作状态
            
            if day_sta == "unwork":     # !!!会忽略在该天的任务info
                rst += self.html_td(day_sta, plan_status, 0)*2  # 连续2个'半小时'填充
                day += day1
                continue
            
            for hhour, hhour_st in enumerate(DAY_HOUR_STA[day_sta].hhour):  # 循环各个'半小时'
                if hhour_st.style == "unwork":  # 非工作'半小时'
                    rst += self.html_td("unwork", plan_status, 0)
                    continue
                hhour = self.hhour_datetime(hhour, day)
                plan_status = self.plan_status(hhour)
                if plan_status == -1:   # 任务已经完成
                    rst += '<td class="{0}"></td>'.format(hhour_st.style)
                    continue
                info, nxt = self.info_status(hhour, info_idx)
                if nxt: info_idx += 1
        
                if info == None: 
                    rst += self.html_td(hhour_st.style, plan_status, 0)
                else:
                    rst += self.html_td(hhour_st.style, plan_status, info.complete, info.id)
                    
            day += day1
            
        return rst
        
        