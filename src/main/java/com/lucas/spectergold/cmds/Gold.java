package com.lucas.spectergold.cmds;

import java.sql.SQLException;
import java.util.List;

import com.lucas.spectergold.api.events.EconomyGoldPayEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lucas.spectergold.Main;
import com.lucas.spectergold.system.PGold;
import com.lucas.spectergold.utils.Formater;

public class Gold implements CommandExecutor{

	@Override
	public boolean onCommand(final CommandSender sender, Command arg1, String label,
			String[] args) {
		Player p = null;
		if (sender instanceof Player){
			p = (Player) sender;
		}
		if (args.length == 2){
			if (args[0].equalsIgnoreCase("delete")){
				if (p != null){
					if (!p.hasPermission("spectereconomy.admin")){
						p.sendMessage("§cVocê não tem permissão!");
						return true;
					}
				}
				String player = args[1];
				if (Main.getGoldCore().getCached(player) != null){
					PGold pm = Main.getGoldCore().getCached(player);
					pm.setGold(0);
					try {
						pm.save(true);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sender.sendMessage("§fConta de §e" + player + "§f resetada.");
				} else {
					sender.sendMessage("§fConta não encontrada.");
				}
				return true;
			}
			sender.sendMessage("§fArgumento inválido, utilize o comando §f/money help§f para obter ajuda.");
		}
		if (args.length == 0){
			// Saldo
			if (p != null){
				PGold pm = Main.getGoldCore().getCached(p.getName());
				if (pm == null){
					p.sendMessage("§fVocê não possui uma conta!");
				} else {
					p.sendMessage("§fSeu saldo é de: §e" + pm.getGold());
				}
			} else {
				sendHelp(sender);
			}
			return true;
		}
		if (args.length == 1){ // Help and saldo de outros
			if (args[0].equalsIgnoreCase("help")){
				if (p == null){
					sendHelp(sender);
				} else {
					if (!p.hasPermission("spectereconomy.admin"))
						sendPHelp(p);
					else {
						sendHelp(sender);
					}
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("top")){ // Criar sistema de top com api.
				for (String a : Main.getInstance().getConfig().getStringList("mensagens.top_list.header")) {
					sender.sendMessage(a.replaceAll("&", "§"));
				}
				List<PGold> pm = Main.getGoldCore().getTopList();
				for (int a = 0; a < pm.size(); a++){
					PGold b = pm.get(a);
					sender.sendMessage(Main.getInstance().getConfig().getString("mensagens.top_list.value")
							.replaceAll("&", "§")
							.replace("@position", "" + (a + 1))
							.replace("@player", (b.getPlayer()))
							.replace("@saldo", Formater.format(b.getGold())));
				}
				for (String a : Main.getInstance().getConfig().getStringList("mensagens.top_list.footer")) {
					sender.sendMessage(a.replaceAll("&", "§"));
				}
				return true;
			}
			final String player = args[0];
			boolean achou = false;
			if (Main.getGoldCore().getChache().containsKey(player.toLowerCase())) {
				PGold pm = Main.getGoldCore().getCached(player);
				achou = true;
				sender.sendMessage("§e" + player + "§fpossui §e" + pm.getGold());
			}
			if (!achou){
				sender.sendMessage("§cJogador inválido!");
			}
			return true;
		}
		if (args.length == 3){
			if (args[0].equalsIgnoreCase("give")){
				if (p != null){
					if (!p.hasPermission("spectereconomy.admin")){
						p.sendMessage("§cVocê não tem permissão!");
						return true;
					}
				}
				String player = args[1];
				if (Main.getGoldCore().getCached(player) != null){
					PGold pm = Main.getGoldCore().getCached(player);
					String valor = args[2];
					if (isDouble(valor)){
						pm.setGold(pm.getGold() + Double.valueOf(valor));
						sender.sendMessage("§fVocê givou" + args[2] + "§f para §e" + player);
						Main.getGoldCore().checkFila(pm);
					} else {
						sender.sendMessage("§cO valor do money deve ser um número válido!");
					}
				} else {
					sender.sendMessage("§cJogador inválido!");
				}
				return true;
			}
            if (args[0].equalsIgnoreCase("remove")){
                if (p != null){
                    if (!p.hasPermission("spectereconomy.admin")){
                        p.sendMessage("§cVocê não tem permissão!");
                        return true;
                    }
                }
                String player = args[1];
                if (Main.getGoldCore().getCached(player) != null){
                    PGold pm = Main.getGoldCore().getCached(player);
                    String valor = args[2];
                    if (isDouble(valor)){
                        pm.setGold(pm.getGold() - Double.valueOf(valor));
                        if (pm.getGold() < 0) {
                            pm.setGold(0);
                        }
                        sender.sendMessage("§fVocê removeu §e" + args[2] + "§f de §e" + player);
                        Main.getGoldCore().checkFila(pm);
                    } else {
                        sender.sendMessage("§cO valor do money deve ser um número válido!");
                    }
                } else {
                    sender.sendMessage("§cJogador inválido!");
                }
                return true;
            }
			if (args[0].equalsIgnoreCase("set")){
				if (p != null){
					if (!p.hasPermission("spectereconomy.admin")){
						p.sendMessage("§cVocê não tem permissão!");
						return true;
					}
				}
				String player = args[1];
				if (Main.getGoldCore().getCached(player) != null){
					PGold pm = Main.getGoldCore().getCached(player);
					String valor = args[2];
					if (isDouble(valor)){
						pm.setGold(Double.valueOf(valor));
						Main.getGoldCore().checkFila(pm);
						sender.sendMessage("§fVocê setou §e" + args[2] + "§f para §e" + player);
					} else {
						sender.sendMessage("§cO valor do money deve ser um número válido!");
					}
				} else {
					sender.sendMessage("§cJogador inválido!");
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("enviar") || args[0].equalsIgnoreCase("pay")){
				if (p != null){
					String player = args[1];
					if (player.equalsIgnoreCase(p.getName())){
						p.sendMessage("§cVocê não pode enviar money para si mesmo.");
						return true;
					}
					String valor = args[2];
					Player o = Bukkit.getPlayer(player);

					if (o == null){
						p.sendMessage("§cJogador inválido!");
						return true;
					}

					PGold pm = Main.getGoldCore().getCached(p.getName());
					PGold om = Main.getGoldCore().getCached(player);

					if (pm == null || om == null){
						p.sendMessage("§cOcorreu um problema!");
					} else {
						if (isDouble(valor)){
							double value = Double.valueOf(valor);
                            EconomyGoldPayEvent empe = new EconomyGoldPayEvent(p, o, value);
                            Bukkit.getPluginManager().callEvent(empe);
                            if (empe.isCancelled()) {
                                return true;
                            } else {
                                value = empe.getGold();
                            }
							if (pm.getGold() >= value){
								pm.setGold(pm.getGold() - value);
								om.setGold(om.getGold() + value);
								p.sendMessage("§fVocê enviou §e" + args[2] + "§f para §e" + player);
								o.sendMessage("§fVocê recebeu §e" + args[2] + "§f de §e" + player);
								Main.getGoldCore().checkFila(pm);
								Main.getGoldCore().checkFila(om);
							} else {
								p.sendMessage("§fVocê não possui money suficiente para fazer isto!");
							}
						} else {
							p.sendMessage("§cO valor do money deve ser um número válido!");
						}
					}
				} else {
					sender.sendMessage("§cInsuportado.");
				}
				return true;
			}
			sender.sendMessage("§fArgumento inválido, utilize o comando §f/money help§f para obter ajuda.");
		}
		sender.sendMessage("§fArgumento inválido, utilize o comando §f/money help§f para obter ajuda.");
		return false;
	}
	public void sendHelp(CommandSender sender){
        sender.sendMessage("§e§lSPECTERGOLD §8-§7 Lista de comandos");
        sender.sendMessage("");
		sender.sendMessage(" §e§o/gold help §7: Mostrar os comandos"); //ok
		sender.sendMessage(" §e§o/gold <player> §7: Mostrar o saldo de alguém"); //ok
		sender.sendMessage(" §e§o/gold give <player> <qnt> §7: Givar money para um jogador"); //ok
        sender.sendMessage(" §e§o/gold remove <player> <qnt> §7: Remover uma quantia de money"); //ok
		sender.sendMessage(" §e§o/gold delete <player> §7: Zerar a conta de algum jogador");
		sender.sendMessage(" §e§o/gold set <jogador> <qnt> §7: Setar o saldo de alguém"); //ok
		sender.sendMessage(" §e§o/gold enviar <player> <qnt> §7: Enviar uma quantia de money para algum player"); //ok
		sender.sendMessage(" §e§o/gold top §7: Rank dos jogadores mais ricos");
        sender.sendMessage("");
	//	sender.sendMessage("§a /gold stats §7§o: Mostrar o status de armazenamento."); //ok
	}
	public void sendPHelp(Player p){
		p.sendMessage("§e§lSPECTERGOLD §8-§7 Lista de comandos");
		p.sendMessage("");
		p.sendMessage(" §e§o/gold help §7: Mostrar os comandos");
		p.sendMessage(" §e§o/gold <player> §7: Mostrar o saldo de alguém");
		p.sendMessage(" §e§o/gold enviar <player> <qnt> §7: Enviar uma quantia de money para algum player");
		p.sendMessage(" §e§o/gold top §7: Rank dos jogadores mais ricos");
		p.sendMessage("");
	}
	public boolean isDouble(String valor){
		if (valor.contains("NaN")) {
			return false;
		}
		try {
			double d = Double.valueOf(valor);
			if (d <= 0){
				return false;
			}
		} catch (NumberFormatException e){
			return false;
		}
		return true;
	}

}
