from flask import Flask, render_template, url_for, request
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

@app.route('/add_task', methods=['POST'])
def add_task():
    if request.method == 'POST':
        return request.form['title']

if __name__ == '__main__':
    app.run(debug=True)