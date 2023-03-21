import uvicorn
import requests
from fastapi import FastAPI, Request, Form, responses
from fastapi.templating import Jinja2Templates
from starlette import status
from starlette.responses import HTMLResponse, RedirectResponse
import datetime


app = FastAPI()
templates = Jinja2Templates(directory="templates/")

users = {"xxx": "xxx", "cat": "lover"}
languages = {"english": "eng", "german": "ger", "ukrainian": "ukr", "spanish": "esp", "filipino": "fil", "urdu": "urd"}
quotes: [] = []
logged = False


def check_parameters(number, _language):
    result: dict = {}
    is_int = isinstance(number, int)
    if is_int:
        is_int = 0 < number < 5
    result["number"] = (is_int, number)
    result["language"] = (_language in languages.keys(), languages.get(_language))
    return result

def check_date_days(date: str, days: int):
    days_okey = isinstance(days, int) and days >= 0
    print(days_okey)
    correct_form_date = datetime.datetime.strptime(date, "%Y-%m-%d").date()
    today = datetime.datetime.now().date()
    date_okey = today >= correct_form_date
    print(date_okey)
    return days_okey, date_okey


def get_start(date: str, days: int):
    return datetime.datetime.strptime(date, "%Y-%m-%d") - datetime.timedelta(days=days)

def get_list(date:str, days: int):
    result = []
    for i in range(days + 1):
        result.append(datetime.datetime.strptime(date, "%Y-%m-%d") - datetime.timedelta(days=i))
    return result


@app.get("/", response_class=HTMLResponse)
async def root(request: Request):
    return templates.TemplateResponse("login.html", {"request": request})


@app.post("/login/", response_class=RedirectResponse)
async def login_form(request: Request, username: str = Form(...), password: str = Form(...)):
    global logged
    if username in users.keys() and users.get(username) == password:
        logged = True
        return responses.RedirectResponse("/param", status_code=status.HTTP_303_SEE_OTHER)
    else:
        return templates.TemplateResponse("login.html", {"request": request})


@app.get("/param", response_class=HTMLResponse)
def cat_param(request: Request):
    if logged:
        return templates.TemplateResponse("basic.html", {"request": request})



@app.post("/facts", response_class=HTMLResponse)
async def facts(request: Request, days: int = Form(...), date: str = Form(...), number: int = Form(...), language: str = Form(...)):
    print("laguage:", language, "number:", number)
    print("Days:", days, "date:", date)
    args = check_parameters(number, language)
    # print(args)
    day_date = check_date_days(date, days)
    if args.get("number")[0] and args.get("language")[0] and day_date[0] and day_date[1]:
        print("oki")
        number = args.get("number")[1]
        _language = args.get("language")[1]
        quotes = []
        if _language == "eng":
            try:
                response = requests.get(f"http://meowfacts.herokuapp.com/?count={number}")
            except Exception as e:
                return templates.TemplateResponse("item.html", context={'request': request,
                                                                        'message': 'Wystąpił błąd z połączeniem z API'})
            if response.status_code != 200:
                return templates.TemplateResponse("item.html",
                                                  context={'request': request,
                                                           'message': 'Wystąpił błąd po stronie zewnetrznego API'})
            response_json: dict = response.json()
            if 'data' not in response_json:
                return templates.TemplateResponse("item.html",
                                                  context={'request': request,
                                                           'message': 'Wystąpił błąd po stronie zewnetrznego API'})
            quotes = response_json["data"]
        else:
            while len(quotes) < number:
                try:
                    response = requests.get(f"https://meowfacts.herokuapp.com/?lang={_language}")
                except Exception as e:
                    return templates.TemplateResponse("item.html", context={'request': request,
                                                                            'message': 'Wystąpił błąd z połączeniem z API'})
                if response.status_code != 200:
                    return templates.TemplateResponse("item.html",
                                                      context={'request': request,
                                                               'message': 'Wystąpił błąd po stronie zewnetrznego API'})
                response_json: dict = response.json()
                if 'data' not in response_json:
                    return templates.TemplateResponse("item.html",
                                                      context={'request': request,
                                                               'message': 'Wystąpił błąd po stronie zewnetrznego API'})
                q: str = response_json["data"][0]
                if q not in quotes:
                    quotes.append(q)
        items = {str(i + 1): quotes[i] for i in range(len(quotes))}

        try:
            start = get_start(date, days)
            response = requests.get(f"https://earthquake.usgs.gov/fdsnws/event/1/count?starttime={start}&endtime={date}")
            all = int(response.json())
            dates = get_list(date, days)
            days_resp = {}
            for d in dates:
                print(d)
                resp = requests.get(f"https://earthquake.usgs.gov/fdsnws/event/1/count?starttime={d}&endtime={d}")
                days_resp[d] = int(resp.json())
                print(type(days_resp.get(d)))
            mean = all / (days + 1)
            max_date = max([(d, val) for i, val in days_resp.items()], key=lambda x: x[1])

        except Exception as e:
            return templates.TemplateResponse("item.html", context={'request': request,
                                                                    'message': 'Wystąpił błąd z połączeniem z API earth'})
        if response.status_code != 200:
            return templates.TemplateResponse("item.html",
                                              context={'request': request,
                                                       'message': 'Wystąpił błąd po stronie zewnetrznego API earth'})

        return templates.TemplateResponse("result.html", context={'request': request, 'mean': mean, "max": max_date[1], 'items': items})
    return templates.TemplateResponse("basic.html", {"request": request})



if __name__ == '__main__':
    uvicorn.run("app:app", reload=True)
