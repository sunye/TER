class Main{
    private static Map parser(String line){
        def VMmap = [:]
        while(line.length() > 0) {
            def key = ""
            def value = ""
            boolean version = false
            while (line.take(1) != "," && line.length() > 0 ) {
                if (line.take(1) == ":" && line.length() > 0) {
                    version = true;
                    line = line.drop(1)
                } else {
                    if (version) {
                        value += line.take(1);
                    } else {
                        key += line.take(1);
                    }
                    line = line.drop(1);
                }
            }
            line = line.drop(1);
            if(value == ""){
                value = "null"
            }
            VMmap[key] = value
        }
        return VMmap
    }
    /* private static void deploy(Map vm,Map needs,String provider){
         switch(needs["deploymentType"]){
             case "nodes":
                 Noeud.deployNode(vm,needs)
                 break;
             case "elements":
                 Element.deployElement(vm,needs)
                 break;
             default:
                 Element.deployElement(vm,needs)
                 break;
         }
     }
 */

    def static Node = {nodeMap,provider,VM -> Noeud.deployNoeud(nodeMap,VM, provider )}
    def static Element = {elemToDeploy, provider, VM -> Element.deployElement(elemToDeploy, provider, VM)}
    def static deploy(action){
        [the:{node ->
            [with:{provider->
                [on:{vmname -> action(node,provider,vmname)}]
            }]
        }]
    }
    private static NeedsText,SPvms;
    private spvmMAP
    static void main(String[] args){
        /*
        def jsonSlurper = new JsonSlurper()
        def data = jsonSlurper.parse(new File("needs3.json"))
        //def VmMap = data["deployement_info"]["provider"];
        for(def k in data.keySet()){
            def NodeMap = data.get(k).Nodes
            def elemToDeploy = data.get(k).element
            if (NodeMap != null) {
                Noeud.deployNoeud(NodeMap, data.get(k).VM, data.get(k).provider)
            }
            if(elemToDeploy!=null) {
                Element.deployElement(elemToDeploy, data.get(k).VM, data.get(k).provider)
            }
        }
    */
        if(args.length == 10){
            if(args[3] == "Node"){
                deploy Node the args[5] with args[7] on args[9]
            }else if (args[3] == "Element"){
                deploy Element the args[5] with args[7] on args[9]
                // deploy  the args[3] with args[5] on args[7]
            }
        }else{
            if(args.length > 0){
                println "missing args"
                for (def k in args){
                    println k
                }
                println args.length
                return
            }
        }
        //main classique
        // deploy Node the TestSuite with local on /home/paci/Documents/TER-Deployment/needs3.json
        deploy Node the "TestSuite" with "local" on "/home/paci/Documents/TER-Deployment/needs3.json"
        //   deploy Elem the "java" with "Amazon" on "VM1"

    }
}
