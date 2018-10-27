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
    tasks = Task.query.filter_by(pid=id).all()
    tasks = [('Task' + '_'.join(map(str, idx)), t.title, t.id) for idx,t in iterator(tasks, gnxt=task_gnxt)]
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