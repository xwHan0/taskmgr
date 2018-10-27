from flask import render_template, redirect, url_for

from app import *

@app.route('/')
def index():
    return render_template('gantt.html',gantt=[])

@app.route('/task/<int:id>')
def view_task(id):
    tasks = Task.query.filter_by(pid=id).all()
    return tasks[0].sub.all()[0].__repr__()
    
@app.route('/add')
def add_task():
    u = Task()
    u.pid = 0
    u.title = 'TaskX'
    db.session.add(u)
    db.session.commit()
    return redirect(url_for('index'))