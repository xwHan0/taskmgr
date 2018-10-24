from flask import Flask, render_template, url_for, request, redirect
from flask-sqlalchemy import SQLAlchemy

from gantt import Gantt
from task import Task, addTask

app = Flask(__name__, static_folder='resources/public', template_folder='templates')

app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:////resources/database/tmgr.sqlite'
    
db = SQLAlchemy(app)

@app.route('/')
def index():
    gat = Gantt()
    tsk = Task("")
    tsk.tid = 1
    tsk.read_plan()
    tsk.read_info()
    html = tsk.html_hhour(gat.start, gat.finish)
    return render_template('gantt.html', gantt = gat, tasks=[html])

@app.route('/add_task', methods=['POST'])
def add_task():
    if request.method == 'POST':
        addTask(request.form['title'])
        # return request.form['title']
        return redirect(url_for('index'))

