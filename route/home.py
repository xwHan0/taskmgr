from flask import render_template, redirect, url_for
from sqlalchemy import and_, or_, func
from datetime import *

from comlib import iterator,Index

from taskmgr.app import app, db
from taskmgr.model.task import *
from taskmgr.model.information import Information

def task_gnxt(node, idx):
    if idx == []:
        return node
    else:
        return node.sub

@app.route('/')
def index():
    return render_template('gantt.html',gantt=[])

@app.route('/task/<int:id>')
def view_task(id):
    t = datetime.today()    # 获取日期

    start = t - timedelta(days = 7 + t.weekday())
    finish = (start+timedelta(days=21)).replace(hour=23, minute=59)

    # 查询开始信息

    # e = Information.query(
    #         Information.tid,
    #         Information.status,
    #         Information.owner,
    #         func.max(Information.finish).label('finish')
    #     ) \
    #     .group_by(Information.tid)  \
    #     .subquery()
    # tasks = Task.query(Task.id,Task.title)  \
    #     .outerjoin(e, e.c.id==Task.id)  \
    #     .outerjoin(s, s.c.id==Task.id)  \
    #     .filter(e.c.status.is_(None), e.c.status!='close')  \
    #     .filter(and_(s.c.start<=start, e.c.finish>=start),
    #         and_(e.c.finish>=finish, s.c.start<=finish),
    #         and_(s.c.start>=start, e.c.finish<=finish))     \
    #     .all()


    # 获取任务信息
    #sinfo = aliased( Information )
    #einfo = aliased( Information )
    
    #tasks = db.session.query(Task)  \
        #.outerjoin(Task.sid == sinfo.id) \
        #.outerjoin(Task.eid == einfo.id) \
        #.filter( Task.pid == id ) \
        #.filter( einfo.status != 'close' ) \
        #.filter( and_(sinfo.start<=start, einfo.finish>=start), \ 
            #and_(sinfo.start<=finish, einfo.finish>=finish), \
            #and_(sinfo.start>=start, einfo.finish<=finish))
        
    #tasks = Task.query.filter_by(pid==id).filter(status！='close').all()
    
    # einfo = db.session.query(
    #     Information.id, Information.tid, Information.status, 
    #     func.max(Information.finish).label('finish')
    # )  \
    # .filter(Information.type=='plan')  \
    # .group_by(Information.tid)  \
    # .subquery()
    
    # sinfo = db.session.query(
    #     Information.id, Information.tid,
    #     func.min(Information.start).label('start')
    # )  \
    # .filter(Information.type=='plan')  \
    # .group_by(Information.tid)  \
    # .subquery()
    
    tasks = Task.query  \
        .filter(Task.pid==id)  \
        .all()
        # .filter(or_(Task.eplan==None, Task.eplan.status=='close')) \
        # .outerjoin(einfo, einfo.c.tid == Task.id)  \
        # .filter(or_(einfo.c.status=='open', einfo.c.status==None))  \
    
    # selection = 'Task[(#1.lvl()==0 and #0.pid==' + str(id) + ') or #1.lvl() > 0]'
    selection = '*'
    tasks = [t.complete(idx.idx()) for t,idx in iterator(tasks, selection, [getattr,'sub']).assist(Index())]
    # 获取日期信息
    
    
    
    return render_template('gantt.html', tasks=tasks, gantt=[])
    # return str(tasks)
    
@app.route('/add')
def add_task():
    u = Task()
    u.pid = 0
    u.title = 'TaskX'
    db.session.add(u)
    db.session.commit()
    return redirect(url_for('index'))