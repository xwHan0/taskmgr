from flask import Flask, render_template, url_for
from gantt import gantt

app = Flask(__name__, static_folder='resources/public', template_folder='templates')




@app.route('/')
def index():
    gat = gantt()
    return render_template('gantt.html', gantt = gat)


if __name__ == '__main__':
    app.run(debug=True)