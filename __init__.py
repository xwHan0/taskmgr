from flask import Flask, render_template, url_for
from gantt import gantt
from task import Task

app = Flask(__name__, static_folder='resources/public', template_folder='templates')


gat = gantt()
tsk = Task("")
tsk.tid = 1
tsk.read_plan()
tsk.read_info()
html = tsk.html_hhour(gat.start, gat.finish)

@app.route('/')
def index():
    gat = gantt()
    tsk = Task("")
    tsk.tid = 1
    tsk.read_plan()
    tsk.read_info()
    html = tsk.html_hhour(gat.start, gat.finish)
    return render_template('gantt.html', gantt = gat, tasks=[html])

@app.route('/add_task')
def add_task():
    return render_template('new_task.html')

if __name__ == '__main__':
    app.run(debug=True)