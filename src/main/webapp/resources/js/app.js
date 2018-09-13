(function f () {
  var ua = navigator.userAgent
  var app = ua.indexOf('LotteryApp') >= 0
  if (app) {
    document.documentElement.classList.add('app')
  }
})()

function timer (dis, cb, onend) {

  tick()
  var timer = setInterval(tick, 1000)

  function tick () {
    dis -= 1
    if (dis <= 0) {
      clearInterval(timer)
      onend && onend()
      return
    }
    cb(dis)
  }

  return timer
}

function toast (msg, duration) {
  duration = isNaN(duration) ? 3000 : duration
  var m = document.createElement('div')
  m.innerHTML = msg
  m.style.cssText = 'position:fixed;z-index:50;top:180px;left:50%;background:rgba(18, 18, 18, 0.7);padding:.5rem 1rem;transform:translateX(-50%);border-radius:5px;color:#fff'
  document.body.appendChild(m)
  setTimeout(function () {
    var d = 0.5
    m.style.webkitTransition = '-webkit-transform ' + d + 's ease-in, opacity ' + d + 's ease-in'
    m.style.opacity = '0'
    setTimeout(function () { document.body.removeChild(m) }, d * 1000)
  }, duration)
  return m
}

function parseQuery () {
  var query = {}
  var vars = location.search.substring(1).split('&')
  for (var i = 0; i < vars.length; i++) {
    var pair = vars[i].split('=')
    query[decodeURIComponent(pair[0])] = decodeURIComponent(pair[1] || '')
  }
  return query
}

function elementIndex (el) {
  var i = 0
  while ((el = el.previousSibling) !== null) {
    i++
  }
  return i
}

/* 触发DOM事件 */
function triggerEvent (el, name, data) {
  var evt = document.createEvent('Event')
  evt.initEvent(name, true, true)
  data && Object.assign(evt, data)
  el.dispatchEvent(evt)
}

function siblings (elem) {
  var n = (elem.parentNode || {}).firstChild
  var matched = []

  for (; n; n = n.nextSibling) {
    if (n.nodeType === 1 && n !== elem) {
      matched.push(n)
    }
  }

  return matched
}

/* 匹配最近的祖先节点 (使用原生方法) */
function closest (el, selector, ctx) {
  var matchesSelector = el.matches || el.webkitMatchesSelector || el.mozMatchesSelector || el.msMatchesSelector
  do {
    if (el === ctx || matchesSelector.call(el, selector)) {
      return el
    }
  } while (el = el.parentElement)
}

/* 匹配最近的祖先节点 */
function _closest (el, selector, ctx) {
  if (!el) return
  do {
    if (el === ctx || _matches(el, selector)) {
      return el
    }
  } while (el = el.parentNode)
}

/* 判断元素是否匹配给定的CSS选择器 (使用原生方法) */
function matches (el, selector) {
  var matchesSelector = el.matches || el.webkitMatchesSelector || el.mozMatchesSelector || el.msMatchesSelector
  if (!matchesSelector) {
    return _matches(el, selector)
  }
  return matchesSelector.call(el, selector)
}

/* 判断元素是否匹配给定的CSS选择器 */
function _matches (el, selector) {
  if (!el) return false
  selector = selector.split('.')
  var tag = selector.shift().toUpperCase(),
    re = new RegExp('\\s(' + selector.join('|') + ')(?=\\s)', 'g')
  return (
    (tag === '' || el.nodeName.toUpperCase() === tag) &&
    (!selector.length || ((' ' + el.className + ' ').match(re) || []).length === selector.length)
  )
}

if (window.FastClick) {
  FastClick.attach(document.body)
}

function isApp() {
  var ua = navigator.userAgent
  var app = ua.indexOf('LotteryApp') > -1
  var android = ua.indexOf('Android') > -1
  var ios = ua.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/) //ios终端
  return app || android || ios
}