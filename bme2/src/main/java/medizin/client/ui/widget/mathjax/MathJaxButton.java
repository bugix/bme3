package medizin.client.ui.widget.mathjax;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Button;

public class MathJaxButton extends Button {

	private static final String ICON_HTML_OPEN = "<span class=\"";
	private static final String ICON_HTML_CLOSE = "\"></span>";
	private static final String BASE_CLASS = " MyMathJax ";
	private String icon = "times";
	private String value = "\times";
	private String group = "MyMath20";
	
	public MathJaxButton() {
		super();
	}
	
	public void setValues(Group group,Symbol symbol) {
		this.group = group.getName();
		this.icon = symbol.getName();
		this.value = symbol.getValue();
		construct();
	}
	
	private void construct() {
		String html = ICON_HTML_OPEN + BASE_CLASS +group + " " + group+"-"+icon;
		html += ICON_HTML_CLOSE;
		removeStyleName("gwt-Button");
		addStyleName("MathJax-Button");
		SafeHtmlBuilder builder = new SafeHtmlBuilder();
		builder.appendHtmlConstant(html);
		super.setHTML(builder.toSafeHtml());
	}
	
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	
	public enum Group {
		
		MyMath20("MyMath20"),MySymbol20("MySymbol20"),MyArrow20("MyArrow20"),MyLogic20("MyLogic20"),MyGreek20("MyGreek20"),MyFun4020("MyFun4020"),MyMath3528("MyMath3528"),MyMath3545("MyMath3545"),MyMathHeigh45("MyMathHeigh45");

		
		private String name;
		
		private Group(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
	};
	
	public interface Symbol {
		String getName();
		String getValue();
	};
	
	public enum MyMath20Symbol implements Symbol{
		times("times","\\times"),
		div("div","\\div"),
		pm("pm","\\pm"),
		mp("mp","\\mp"),
		cdot("cdot","\\cdot"),
		star("star","\\star");
		
		private String name,value;
		
		private MyMath20Symbol(String name,String value) {
			this.name = name;
			this.value = value;
		}
		
		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}

	};
	
	public enum MyMath3528Symbol  implements Symbol{

		sqrt("sqrt","\\sqrt{ab}"),
		sqrtn("sqrtn","\\sqrt[x]{ab}"),
		log("log","\\log_{a}{b}"),
		lg("lg","\\lg{ab}"),
		abUP("abUP","a^{b}"),
		abDown("abDown","a_{b}"),
		UpAndDown("UpAndDown","c_{a}^{b}"),
		widetilde("widetilde","\\widetilde{ab}"),
		widehat("widehat","\\widehat{ab}"),
		overleftarrow("overleftarrow","\\overleftarrow{ab}"),
		overrightarrow("overrightarrow","\\overrightarrow{ab}"),
		overbrace("overbrace","\\overbrace{ab}"),
		underbrace("underbrace","\\underbrace{ab}"),
		underline("underline","\\underline{ab}"),
		overline("overline","\\overline{ab}");
		
		private String name,value;
		
		private MyMath3528Symbol(String name,String value) {
			this.name = name;
			this.value = value;
		}
		
		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}

	};
	
	public enum MyMath3545Symbol  implements Symbol{

		fracA("fracA","\\frac{ab}{cd}"),
		fracB("fracB","\\frac{\\partial ab}{\\partial cd}"),
		fracC("fracC","\\frac{\\text{dx}}{\\text{dy}}"),
		lim("lim","\\lim_{a \\rightarrow b}"),
		intA("intA","\\int_{a}^{b} "),
		oint("oint","\\oint_{a}^{b}"),
		prod("prod","\\prod_{a}^{b}"),
		coprod("coprod","\\coprod_{a}^{b}"),
		bigcap("bigcap","\\bigcap_{a}^{b}"),
		bigcup("bigcup","\\bigcup_{a}^{b}"),
		bigvee("bigvee","\\bigvee_{a}^{b}"),
		bigwedge("bigwedge","\\bigwedge_{a}^{b}"),
		bigsqcup("bigsqcup","\\bigsqcup_{a}^{b}"),
		sumA("sumA","\\sum_{a}^{b}"),
		arrayA("arrayA","\\left(\\begin{array}{c}\\\\ \\end{array}\\right)");
		
		private String name,value;
		
		private MyMath3545Symbol(String name,String value) {
			this.name = name;
			this.value = value;
		}
		
		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}
	};
	
	public enum MyMathHeigh45Symbol implements Symbol{

		bmatrix("bmatrix","\\begin{bmatrix} &amp;  \\\\ &amp;  \\end{bmatrix}"),
		casesA("casesA","\\begin{cases} &amp;\\\\ &amp; \\end{cases}");
		
		private String name,value;
		
		private MyMathHeigh45Symbol(String name,String value) {
			this.name = name;
			this.value = value;
		}
		
		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}

	};
	
	public enum MyGreek20Symbol implements Symbol {
		
		alpha("alpha","\\alpha"),
		beta("beta","\\beta"),
		gamma("gamma","\\gamma"),
		delta("delta","\\delta"),
		epsilon("epsilon","\\epsilon"),
		zeta("zeta","\\zeta"),
		eta("eta","\\eta"),
		theta("theta","\\theta"),
		iota("iota","\\iota"),
		kappa("kappa","\\kappa"),
		lambda("lambda","\\lambda"),
		mu("mu","\\mu"),
		nu("nu","\\nu"),
		xi("xi","\\xi"),
		pi("pi","\\pi"),
		rho("rho","\\rho"),
		sigma("sigma","\\sigma"),
		tau("tau","\\tau"),
		upsilon("upsilon","\\upsilon"),
		phi("phi","\\phi"),
		chi("chi","\\chi"),
		psi("psi","\\psi"),
		omega("omega","\\omega");
		
		private String name,value;
		
		private MyGreek20Symbol(String name,String value) {
			this.name = name;
			this.value = value;
		}
		
		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}
	}
	
	public enum MyFun4020Symbol implements Symbol {
		
		arccos("arccos","\\arccos"),
		arcsin("arcsin","\\arcsin"),
		arctan("arctan","\\arctan"),
		cos("cos","\\cos"),
		cosh("cosh","\\cosh"),
		cot("cot","\\cot"),
		coth("coth","\\coth"),
		csc("csc","\\csc"),
		sec("sec","\\sec"),
		sin("sin","\\sin"),
		sinh("sinh","\\sinh"),
		tan("tan","\\tan"),
		tanh("tanh","\\tanh"),
		exp("exp","\\exp"),
		log("log","\\log"),
		ln("ln","\\ln"),
		max("max","\\max"),
		min("min","\\min"),
		sup("sup","\\sup"),
		inf("inf","\\inf"),
		lim("lim","\\lim"),
		gcd("gcd","\\gcd"),
		hom("hom","\\hom"),
		ker("ker","\\ker"),
		det("det","\\det"),
		bmod("bmod","\\bmod");
		
		private String name,value;
		
		private MyFun4020Symbol(String name,String value) {
			this.name = name;
			this.value = value;
		}
		
		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}
	}
	
	public enum MyLogic20Symbol implements Symbol {
		
		neq("neq","\\neq"),
		leq("leq","\\leq"),
		geq("geq","\\geq"),
		sim("sim","\\sim"),
		approx("approx","\\approx"),
		cong("cong","\\cong"),
		equiv("equiv","\\equiv"),
		propto("propto","\\propto"),
		ll("ll","\\ll"),
		gg("gg","\\gg"),
		in("in","\\in"),
		subset("subset","\\subset"),
		subseteq("subseteq","\\subseteq"),
		prec("prec","\\prec"),
		preceq("preceq","\\preceq"),
		simeq("simeq","\\simeq"),
		asymp("asymp","\\asymp"),
		doteq("doteq","\\doteq"),
		succ("succ","\\succ"),
		succeq("succeq","\\succeq"),
		sqsubseteq("sqsubseteq","\\sqsubseteq"),
		sqsupseteq("sqsupseteq","\\sqsupseteq"),
		ni("ni","\\ni"),
		models("models","\\models"),
		vdash("vdash","\\vdash"),
		dashv("dashv","\\dashv"),
		perp("perp","\\perp"),
		mid("mid","\\mid"),
		parallel("parallel","\\parallel"),
		smile("smile","\\smile"),
		frown("frown","\\frown"),
		bowtie("bowtie","\\bowtie"),
		unlhd("unlhd","\\unlhd"),
		unrhd("unrhd","\\unrhd"),
		hat("hat","\\hat{}"),
		check("check","\\check{}"),
		breve("breve","\\breve{}"),
		acute("acute","\\acute{}"),
		grave("grave","\\grave{}"),
		tilde("tilde","\\tilde{}"),
		bar("bar","\\bar{}"),
		vec("vec","\\vec{}"),
		dot("dot","\\dot{}"),
		ddot("ddot","\\ddot{}");
		
		private String name,value;
		
		private MyLogic20Symbol(String name,String value) {
			this.name = name;
			this.value = value;
		}
		
		public String getName() {
			return name;
		}
	
		public String getValue() {
			return value;
		}
	}
	
	public enum MyArrow20Symbol implements Symbol {
		
		leftL("leftL","\\left("),
		rightL("rightL","\\right)"),
		leftM("leftM","\\left["),
		rightM("rightM","\\right]"),
		leftD("leftD","\\left\\{"),
		rightD("rightD","\\right\\}"),
		lfloor("lfloor","\\lfloor"),
		lceil("lceil","\\lceil"),
		rfloor("rfloor","\\rfloor"),
		rceil("rceil","\\rceil"),
		langle("langle","\\langle"),
		rangle("rangle","\\rangle"),
		backslash("backslash","\\backslash"),
		cap("cap","\\cap"),
		cup("cup","\\cup"),
		uplus("uplus","\\uplus"),
		sqcap("sqcap","\\sqcap"),
		sqcup("sqcup","\\sqcup"),
		vee("vee","\\vee"),
		wedge("wedge","\\wedge"),
		wr("wr","\\wr"),
		leftarrow("leftarrow","\\leftarrow"),
		DLeftarrow("DLeftarrow","\\Leftarrow"),
		rightarrow("rightarrow","\\rightarrow"),
		DRightarrow("DRightarrow","\\Rightarrow"),
		leftrightarrow("leftrightarrow","\\leftrightarrow"),
		DLeftrightarrow("DLeftrightarrow","\\Leftrightarrow"),
		mapsto("mapsto","\\mapsto"),
		leftharpoonup("leftharpoonup","\\leftharpoonup"),
		leftharpoondown("leftharpoondown","\\leftharpoondown"),
		rightleftharpoons("rightleftharpoons","\\rightleftharpoons"),
		leftrightharpoons("leftrightharpoons","\\leftrightharpoons"),
		hookrightarrow("hookrightarrow","\\hookrightarrow"),
		rightharpoonup("rightharpoonup","\\rightharpoonup"),
		uparrow("uparrow","\\uparrow"),
		DUparrow("DUparrow","\\Uparrow"),
		downarrow("downarrow","\\downarrow"),
		DDownarrow("DDownarrow","\\Downarrow"),
		updownarrow("updownarrow","\\updownarrow"),
		DUpdownarrow("DUpdownarrow","\\Updownarrow"),
		leftleftarrows("leftleftarrows","\\leftleftarrows"),
		rightrightarrows("rightrightarrows","\\rightrightarrows"),
		leftrightarrows("leftrightarrows","\\leftrightarrows"),
		rightleftarrows("rightleftarrows","\\rightleftarrows"),
		Lleftarrow("Lleftarrow","\\Lleftarrow"),
		Rrightarrow("Rrightarrow","\\Rrightarrow"),
		twoheadleftarrow("twoheadleftarrow","\\twoheadleftarrow"),
		twoheadrightarrow("twoheadrightarrow","\\twoheadrightarrow"),
		leftarrowtail("leftarrowtail","\\leftarrowtail"),
		rightarrowtail("rightarrowtail","\\rightarrowtail"),
		looparrowleft("looparrowleft","\\looparrowleft"),
		looparrowright("looparrowright","\\looparrowright"),
		curvearrowleft("curvearrowleft","\\curvearrowleft"),
		curvearrowright("curvearrowright","\\curvearrowright"),
		circlearrowleft("circlearrowleft","\\circlearrowleft"),
		circlearrowright("circlearrowright","\\circlearrowright"),
		dashleftarrow("dashleftarrow","\\dashleftarrow"),
		dashrightarrow("dashrightarrow","\\dashrightarrow"),
		Lsh("Lsh","\\Lsh"),
		Rsh("Rsh","\\Rsh"),
		upuparrows("upuparrows","\\upuparrows"),
		downdownarrows("downdownarrows","\\downdownarrows"),
		upharpoonleft("upharpoonleft","\\upharpoonleft"),
		upharpoonright("upharpoonright","\\upharpoonright"),
		downharpoonleft("downharpoonleft","\\downharpoonleft"),
		downharpoonright("downharpoonright","\\downharpoonright"),
		rightsquigarrow("rightsquigarrow","\\rightsquigarrow"),
		leftrightsquigarrow("leftrightsquigarrow","\\leftrightsquigarrow"),
		multimap("multimap","\\multimap"),
		nleftarrow("nleftarrow","\\nleftarrow"),
		nrightarrow("nrightarrow","\\nrightarrow"),
		DnLeftarrow("DnLeftarrow","\\nLeftarrow"),
		DnRightarrow("DnRightarrow","\\nRightarrow"),
		nleftrightarrow("nleftrightarrow","\\nleftrightarrow"),
		DnLeftrightarrow("DnLeftrightarrow","\\nLeftrightarrow");
		
		private String name,value;
		
		private MyArrow20Symbol(String name,String value) {
			this.name = name;
			this.value = value;
		}
		
		public String getName() {
			return name;
		}
	
		public String getValue() {
			return value;
		}
	}
	public enum MySymbols20Symbol implements Symbol {
			
		imath("imath","\\imath"),
		jmath("jmath","\\jmath"),
		ell("ell","\\ell"),
		wp("wp","\\wp"),
		Im("Im","\\Im"),
		prime("prime","\\prime"),
		angle("angle","\\angle"),
		flat("flat","\\flat"),
		natural("natural","\\natural"),
		sharp("sharp","\\sharp"),
		S("S","\\S"),
		checkmark("checkmark","\\checkmark"),
		ulcorner("ulcorner","\\ulcorner"),
		urcorner("urcorner","\\urcorner"),
		llcorner("llcorner","\\llcorner"),
		lrcorner("lrcorner","\\lrcorner"),
		triangle("triangle","\\triangle"),
		triangledown("triangledown","\\triangledown"),
		triangleleft("triangleleft","\\triangleleft"),
		triangleright("triangleright","\\triangleright"),
		forall("forall","\\forall"),
		exists("exists","\\exists"),
		surd("surd","\\surd"),
		top("top","\\top"),
		bot("bot","\\bot"),
		heartsuit("heartsuit","\\heartsuit"),
		backprime("backprime","\\backprime"),
		varnothing("varnothing","\\varnothing"),
		sphericalangle("sphericalangle","\\sphericalangle"),
		nexists("nexists","\\nexists"),
		Game("Game","\\Game"),
		dotplus("dotplus","\\dotplus"),
		ltimes("ltimes","\\ltimes"),
		rtimes("rtimes","\\rtimes"),
		Cap("Cap","\\Cap"),
		Cup("Cup","\\Cup"),
		leftthreetimes("leftthreetimes","\\leftthreetimes"),
		rightthreetimes("rightthreetimes","\\rightthreetimes"),
		curlywedge("curlywedge","\\curlywedge"),
		curlyvee("curlyvee","\\curlyvee"),
		Finv("Finv","\\Finv"),
		diagup("diagup","\\diagup"),
		diagdown("diagdown","\\diagdown"),
		barwedge("barwedge","\\barwedge"),
		veebar("veebar","\\veebar"),
		doublebarwedge("doublebarwedge","\\doublebarwedge"),
		Box("Box","\\Box"),
		boxplus("boxplus","\\boxplus"),
		boxminus("boxminus","\\boxminus"),
		boxtimes("boxtimes","\\boxtimes"),
		boxdot("boxdot","\\boxdot"),
		circledast("circledast","\\circledast"),
		circledcirc("circledcirc","\\circledcirc"),
		divideontimes("divideontimes","\\divideontimes"),
		therefore("therefore","\\therefore"),
		because("because","\\because"),
		oplus("oplus","\\oplus"),
		ominus("ominus","\\ominus"),
		otimes("otimes","\\otimes"),
		oslash("oslash","\\oslash"),
		diamondsuit("diamondsuit","\\diamondsuit"),
		diamond("diamond","\\diamond"),
		circ("circ","\\circ"),
		hslash("hslash","\\hslash"),
		hbar("hbar","\\hbar"),
		mho("mho","\\mho"),
		infty("infty","\\infty"),
		partial("partial","\\partial"),
		eth("eth","\\eth"),
		blacktriangleleft("blacktriangleleft","\\blacktriangleleft"),
		blacktriangleright("blacktriangleright","\\blacktriangleright"),
		blacklozenge("blacklozenge","\\blacklozenge"),
		blacktriangle("blacktriangle","\\blacktriangle"),
		blacktriangledown("blacktriangledown","\\blacktriangledown"),
		blacksquare("blacksquare","\\blacksquare"),
		clubsuit("clubsuit","\\clubsuit"),
		spadesuit("spadesuit","\\spadesuit"),
		bigstar("bigstar","\\bigstar"),
		maltese("maltese","\\maltese");
		
		private String name,value;
		
		private MySymbols20Symbol(String name,String value) {
			this.name = name;
			this.value = value;
		}
		
		public String getName() {
			return name;
		}
	
		public String getValue() {
			return value;
		}
	}		
}
