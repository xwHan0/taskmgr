from flask import render_template, redirect, url_for
from flask_sqlalchemy.orm import aliased
from flask_sqlalchemy import and_, func
import Datatime

from comlib.tensor import iterator

from app import app, db
from model.task import *
from model.information import Information

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
    s = Information.query(
            Information.tid,
            func.min(Information.start).label('start')
        ) \
        .group_by(Information.tid)  \
        .subquery()
    e = Information.query(
            Information.tid,
            Information.status,
            Information.owner,
            func.max(Information.finish).label('finish')
        ) \
        .group_by(Information.tid)  \
        .subquery()
    tasks = Task.query(Task.id,Task.title)  \
        .outerjoin(e, e.c.id==Task.id)  \
        .outerjoin(s, s.c.id==Task.id)  \
        .filter(e.c.status.is_(None), e.c.status!='close')  \
        .filter(and_(s.c.start<=start, e.c.finish>=start),
            and_(e.c.finish>=finish, s.c.start<=finish),
            and_(s.c.start>=start, e.c.finish<=finish))
        .all()


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
    tasks = [t.complete(idx) for idx,t in iterator(tasks, gnxt=task_gnxt)]
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