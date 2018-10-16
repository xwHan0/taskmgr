
class task():

    def __init__(self, name, owner = None, description = None, start = None, finish = None):
        self.name = name
        self.owner owner
        self.description = desription
        self.start = start
        self.finish = finish
        self.info = []
        
    def detail(self, owner, description, start, finish):
        self.owner owner
        self.description = desription
        self.start = start
        self.finish = finish
        
    def html_task(self):
        return '<td>{0}</td><td>{1}</td><td>{2}</td><td>{3}</td>'.format(self.name, self.owner, self.start, self.finish)

    def html_hhour(self, start, finish):
        rst = ""
        
        
        