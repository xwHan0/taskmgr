from flask import render_template, redirect, url_for

from comlib.tensor import iterator

from app import app
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
    # 获取任务信息
    tasks = Task.query.filter_by(pid=id).all()
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